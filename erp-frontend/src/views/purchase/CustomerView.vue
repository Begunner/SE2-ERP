<template>
  <Layout>
    <Title title="客户管理"></Title>
    <el-button type="primary" size="medium" @click="addAnCustomer">新增客户</el-button>
    <div style="margin-top: 10px">
      <el-table
        :data="customerList"
        stripe
        style="width: 100%"
        :header-cell-style="{'text-align':'center'}"
        :cell-style="{'text-align':'center'}">
        <el-table-column
          prop="id"
          label="id"
          width="60">
        </el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="70">
        </el-table-column>
        <el-table-column
          prop="type"
          label="类型"
          width="100"
          :filters="[{ text: '供应商', value: '供应商' }, { text: '销售商', value: '销售商' }]"
          :filter-method="filterTag"
          filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.type === '供应商' ? 'primary' : 'success'"
              disable-transitions>{{scope.row.type}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="level"
          label="级别"
          width="50">
        </el-table-column>
        <el-table-column
          prop="phone"
          label="电话"
          width="150">
        </el-table-column>
        <el-table-column
          prop="address"
          label="地址"
          width="150">
        </el-table-column>
        <el-table-column
          prop="zipcode"
          label="邮编"
          width="100">
        </el-table-column>
        <el-table-column
          prop="email"
          label="邮箱"
          width="200">
        </el-table-column>
        <el-table-column
          prop="lineOfCredit"
          label="应收额度(元)"
          width="120">
        </el-table-column>
        <el-table-column
          prop="receivable"
          label="应收(元)"
          width="120">
        </el-table-column>
        <el-table-column
          prop="payable"
          label="应付(元)"
          width="120">
        </el-table-column>
        <el-table-column
          prop="operator"
          label="操作员"
          width="120">
        </el-table-column>
        <el-table-column
          label="操作">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="delCustomer(scope.row.id)"
              type="text"
              size="small">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
        title="新增客户"
        :visible.sync="dialogVisible"
        width="40%">
      <div style="width: 90%; margin: 0 auto">
        <span>分类:</span><br>
        <el-select name="city" v-model="customerInfo.type">
          <el-option value="供应商">供应商</el-option>
          <el-option value="销售商">销售商</el-option>
        </el-select>
        <br>
        <span>级别:</span><br>
        <el-select name="level" v-model="customerInfo.level">
          <el-option value="1">一级</el-option>
          <el-option value="2">二级</el-option>
          <el-option value="3">三级</el-option>
          <el-option value="4">四级</el-option>
          <el-option value="5">五级</el-option>
        </el-select>
        <br>
        <label>姓名:</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.name"></el-input><br>
        <label>电话:</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.phone"></el-input><br>
        <label>地址:</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.address"></el-input><br>
        <label>邮编(6位):</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.zipcode"></el-input><br>
        <label>电子邮箱:</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.email"></el-input><br>
        <label>应收额度(客户欠本公司的钱不能超过应收额度):</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.lineOfCredit"></el-input><br>
        <label>应收(本公司应收的钱):</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.receivable"></el-input><br>
        <label>应付(本公司应付的钱):</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.payable"></el-input><br>
        <label>默认业务员:</label><br>
        <el-input type="text"  v-model.lazy="customerInfo.operator"></el-input><br>

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">提交新建</el-button>
      </span>
    </el-dialog>
  </Layout>
</template>

<script>
import Layout from "../../components/content/Layout";
import Title from "../../components/content/Title";
import { getAllCustomer } from "../../network/purchase";
import { addCustomer, deleteCustomer } from "../../network/sale";
import {deleteCommodity} from "../../network/commodity";

export default {
  name: 'CustomerView',
  components: {
    Layout,
    Title
  },
  data() {
    return {
      customerList: [],
      customerInfo:{
        type:"",
        level:"",
        name:"",
        phone:"",
        address:"",
        zipcode:"",
        email:"",
        lineOfCredit:"",
        receivable:"",
        payable:"",
        operator:""
      },
      dialogVisible: false
    }
  },
  async mounted() {
    await getAllCustomer({ params : { type: 'SUPPLIER' } }).then(_res => {
      this.customerList = this.customerList.concat(_res.result)
    })
    await getAllCustomer({ params : { type: 'SELLER' } }).then(_res => {
      this.customerList = this.customerList.concat(_res.result)
    })
  },
  methods: {
    filterTag(value, row) {
      return row.type === value
    },
    addAnCustomer() {
      // TODO: 新增客户
      this.dialogVisible = true
    },
    delCustomer(id) {
      // TODO: 删除客户信息


      let config = {
        params: {
          id: id
        }
      };
      this.$confirm('是否要删除该客户？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCustomer(config).then(_res => {
          if (_res.code === "A0002") {
            this.$message({
              type: "error",
              message: "客户不存在！"
            });
            alert("客户不存在！");
            this.userInfo = {};
          } else {
            this.$emit("refresh");
            this.$message({
              type: "success",
              message: "删除成功"
            });
            alert("删除成功！");
          }
        })
      })
      window.location.reload()
    },
    submit(){
      console.log(this.customerInfo)
      this.dialogVisible = false
      addCustomer(this.customerInfo).then(res=>{
        console.log(res)
        if (res.code === 'A0002') {
          alert("客户已存在，请修改相关信息")
          this.userInfo = {};
        } else {
          alert("添加成功！")
          this.userInfo = {};
        }
      })
      window.location.reload()
    }
  }
}
</script>

<style>

</style>
