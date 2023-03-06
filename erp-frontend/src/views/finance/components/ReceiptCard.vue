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

            <span style="margin-left: 10px">
              <el-tag v-if="type === 2" effect="dark" type='success'>审核通过</el-tag>
              <el-tag v-if="type === 3" effect="dark" type='danger'>审核未通过</el-tag>
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
            <span><strong>客户: </strong>{{item.supplier}}</span>
          </el-col>
          <el-col :span="3">
            <span><strong>操作员: </strong>{{item.operator}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>收款总额: </strong>{{item.totalAmount}}(元)</span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <span><strong>创建时间:</strong>{{formatDate(item.createTime)}}</span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="24">
            <span><strong>备注: </strong>{{item.remark}}</span>
          </el-col>
        </el-row>
        <div v-if="showAll[index]" style="margin-top: 15px">
          <div style="margin-bottom: 15px"><strong>转账列表信息:</strong></div>
          <el-table
              :data="item.receiptSheetContent"
              stripe
              style="width: 100%"
              :header-cell-style="{'text-align':'center'}"
              :cell-style="{'text-align':'center'}">
            <el-table-column
                prop="id"
                label="id"
                width="180">
            </el-table-column>
            <el-table-column
                prop="receiptSheetId"
                label="关联的收款单id"
                width="200">
            </el-table-column>
            <el-table-column
                prop="account"
                label="转账账户"
                width="200">
            </el-table-column>
            <el-table-column
                prop="amount"
                label="转账金额"
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
          <label></label>
          <el-form-item
              v-for="(item, index) in copySheet.receiptSheetContent"
              :key="index"
              :label="'账户' + index">
            <div>
              <label>转账金额 </label>
              <el-input v-model="item.amount" style="width: 70%; margin-right: 5%" placeholder="请输入转账金额"></el-input>
            </div>
            <div style="margin-top: 10px">
              <label>账户备注 </label>
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
import { approvalReceipt} from '../../../network/finance'
import { writeBackReceipt } from "../../../network/finance";

export default {
  name: 'ReceiptCard',
  props: {
    list: Array,
    type: Number
  },
  data() {
    return {
      showAll: [],
      tempNum:[],
      copySheet:{},
      dialogVisible: false,
      index:0
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
      if (this.type === 1 && sessionStorage.getItem('role') === 'GM') {
        return 1
      } else if (this.type === 2 && (sessionStorage.getItem('role') === 'FINANCIAL_STAFF' || sessionStorage.getItem('role') === 'ADMIN')) {
        return 3
      }
    },
    approval(id) {
      let config = {
        params: {
          receiptSheetId: id,
          state: 'SUCCESS'
        }
      }
      if (this.type === 1) {
        approvalReceipt(config).then(res => {
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
          receiptSheetId: id,
          state: 'FAILURE'
        }
      }
      if (this.type === 1) {
        approvalReceipt(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      }
    },
    formatDate(gmt) {
      let date = new Date(gmt)
      let m = (date.getMonth() + 1), mm = '-' + (m < 10 ? '0' + m : m)
      let d = date.getDate(), dd = '-' + (d < 10 ? '0' + d : d)
      let h = date.getHours(), hh = ' ' +(h < 10 ? '0' + h : h)
      let i = date.getMinutes(), ii = ':' +(i < 10 ? '0' + i : i)
      let s = date.getSeconds(), ss = ':' +(s < 10 ? '0' + s : s)
      return date.getFullYear() + mm + dd + hh + ii + ss
    },
    writeBack(index){
      let config=JSON.parse(JSON.stringify(this.list[index]));
      config.paymentSheetContent.forEach((item) => {item.amount = -item.amount});
      writeBackReceipt(config).then(res=>{
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
        receiptSheetContent: []
      }
    },
    submitForm(){
      let config=JSON.parse(JSON.stringify(this.list[this.index]));
      config.receiptSheetContent.forEach((item) => {item.amount = -item.amount});
      writeBackReceipt(config).then(res=>{
        this.$emit("refresh")
        this.$message({
          message: '红冲成功!',
          type: 'success'
        })
        let config1=this.copySheet
        writeBackReceipt(config1).then(res=>{
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
