<template>
  <div class="card">
    <el-card v-for="(item, index) in list" :index="item.index" :key="item.id" shadow="hover">
      <template #header>
        <el-row>
          <el-col :span="18">
            <span><strong>id: </strong>{{item.id}}</span>
            <el-button v-if="authorization() === 1" style="margin-left: 10px"
              type="success" icon="el-icon-check" circle size="mini" @click="approval(item.id)"></el-button>
            <el-button v-if="authorization() === 1"
              type="danger" icon="el-icon-close" circle size="mini" @click="deny(item.id)"></el-button>
            <el-button v-if="authorization() === 2" style="margin-left: 10px"
              type="primary" icon="el-icon-check" circle size="mini" @click="approval(item.id)"></el-button>
            <el-button v-if="authorization() === 2"
              type="danger" icon="el-icon-close" circle size="mini" @click="deny(item.id)"></el-button>
            <span style="margin-left: 10px">
              <el-tag v-if="type === 3" effect="dark" type='success'>审核通过</el-tag>
              <el-tag v-if="type === 4" effect="dark" type='danger'>审核未通过</el-tag>
            </span>
          </el-col>
          <el-col :span="6">
            <el-button class="button" type="text"
              v-if="!showAll[index]"
              @click="changeState(index)">
              展开
            </el-button>
            <el-button class="button" type="text"
              v-if="showAll[index]"
              @click="changeState(index)">
            收起</el-button>
            <el-button class="button" type="text"
                       v-if="authorization() === 3"
                       @click="writeBack(index)">
              红冲
            </el-button>
            <el-button class="button" type="text"
                       v-if="authorization() === 3"
                       @click="writeBackWithCopy(index)">
              红冲复制
            </el-button>
          </el-col>
        </el-row>
      </template>
      <div>
        <el-row>
          <el-col :span="3">
            <span><strong>供应商id: </strong>{{item.supplier}}</span>
          </el-col>
          <el-col :span="3">
            <span><strong>操作员: </strong>{{item.operator}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>总额合计: </strong>{{item.totalAmount}}(元)</span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="24">
            <span><strong>备注: </strong>{{item.remark}}</span>
          </el-col>
        </el-row>
        <div v-if="showAll[index]" style="margin-top: 15px">
          <div style="margin-bottom: 15px"><strong>详细商品清单:</strong></div>
          <el-table
            :data="item.purchaseSheetContent"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}">
            <el-table-column
              prop="id"
              label="id"
              width="100">
            </el-table-column>
            <el-table-column
              prop="pid"
              label="商品id"
              width="180">
            </el-table-column>
            <el-table-column
              prop="quantity"
              label="数量"
              width="200">
            </el-table-column>
            <el-table-column
              prop="unitPrice"
              label="单价(元)"
              width="200">
            </el-table-column>
            <el-table-column
              prop="totalPrice"
              label="金额(元)"
              width="200">
            </el-table-column>
            <el-table-column
              prop="remark"
              label="备注">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
    <el-dialog
        title="填写复制单"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose">
      <div style="width: 90%; margin: 0 auto">
        <el-form :model="copySheet" label-width="80px" ref="copySheet">
          <el-form-item
              v-for="(item, index) in copySheet.purchaseSheetContent"
              :key="index"
              :label="'商品' + index">
            <div>
              <label>数量 </label>
              <el-input v-model="item.quantity" style="width: 25%; margin-right: 5%" placeholder="请输入商品数量"></el-input>
              <label>单价 </label>
              <el-input v-model="item.unitPrice" style="width: 25%;" placeholder="请输入商品单价"></el-input>
            </div>
            <div style="margin-top: 10px">
              <label>备注 </label>
              <el-input v-model="item.remark" style="width: 70%; margin-right: 10%" placeholder="请填写备注"></el-input>
            </div>
          </el-form-item>
          <el-form-item label="备注: ">
            <el-input type="textarea" v-model="copySheet.remark"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm()">立即提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { firstApproval, secondApproval } from '../../../network/purchase'
import { writeBackPurchase } from "../../../network/finance";
export default {
  name: "PurchaseList",
  props: {
    list: Array,
    type: Number,
  },
  data() {
    return {
      showAll: [],
      tempNum:[],
      copySheet:{},
      dialogVisible: false,
      index:0,
    }
  },
  mounted() {
    this.showAll = new Array(this.list.length).fill(false)
  },
  methods: {
    changeState(index) {
      this.$set(this.showAll, index, !this.showAll[index])
    },
    authorization() {
      if (this.type === 1 && sessionStorage.getItem('role') === 'SALE_MANAGER') {
        return 1
      } else if (this.type === 2 && sessionStorage.getItem('role') === 'GM') {
        return 2
      } else if (this.type === 3 && (sessionStorage.getItem('role') === 'FINANCIAL_STAFF' || sessionStorage.getItem('role') === 'ADMIN')) {
        return 3
      }
    },
    approval(id) {
      let config = {
        params: {
          purchaseSheetId: id,
          state: this.type === 1 ? 'PENDING_LEVEL_2' : 'SUCCESS'
        }
      }
      if (this.type === 1) {
        firstApproval(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      } else {
        secondApproval(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      }
    },
    deny(id) {
      let config = {
        params: {
          purchaseSheetId: id,
          state: 'FAILURE'
        }
      }
      if (this.type === 1) {
        firstApproval(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      } else {
        secondApproval(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      }
    },
    writeBack(index){
      let config=JSON.parse(JSON.stringify(this.list[index]));
      config.purchaseSheetContent.forEach((item) => {item.quantity = -item.quantity});
      writeBackPurchase(config).then(res=>{
        this.$emit("refresh")
        this.$message({
          message: '红冲成功!',
          type: 'success'
        })
      })
    },
    writeBackWithCopy(index){
      this.dialogVisible=true
      this.index=index
      this.copySheet=JSON.parse(JSON.stringify(this.list[index]))
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            this.resetForm()
            done();
          })
          .catch(_ => {});
    },
    resetForm() {
      this.copySheet = {
        purchaseSheetContent: [
          {
            pid: '',
            quantity: '',
            unitPrice: '',
            remark: ''
          }
        ]
      }
    },
    submitForm(){
      let config=JSON.parse(JSON.stringify(this.list[this.index]));
      config.purchaseSheetContent.forEach((item) => {item.quantity = -item.quantity});
      writeBackPurchase(config).then(res=>{
        this.$emit("refresh")
        this.$message({
          message: '红冲成功!',
          type: 'success'
        })
        let config1=this.copySheet
        writeBackPurchase(config1).then(res=>{
          this.$emit("refresh")
          this.$message({
            message: '复制成功!',
            type: 'success'
          })
        })
        this.dialogVisible=false
      })

    }
  }
}
</script>

<style lang="scss" scoped>
.card {
  width: 80%;
  margin: 0 auto;
  .button {
    float: right;
    margin-left: 20px;
    padding: 3px 0
  }
}
.el-card {
  margin-bottom: 20px;
  background: #EEF7F2;
}
</style>
