<template>
  <Layout>
    <Title title="期初建账"></Title>
    <el-select v-model="id" placeholder="请选择一套帐" @change="getBookById">
      <el-option
          v-for="item in idList"
          :key="item"
          :label="item"
          :value="item">
      </el-option>
    </el-select>
    <el-tag v-if="this.id!==''" style="margin-left: 20px">建账时间: {{formatDate(this.book.createTime)}}</el-tag>
    <el-button @click="create" style="float: right; margin-right: 50px">点击建账</el-button>
    <el-tabs active-name="CUSTOMER" :stretch="true">
      <el-tab-pane label="客户" name="CUSTOMER">
        <div v-if="customerList.length === 0">
          <el-empty description="暂无数据"></el-empty>
        </div>
        <div v-else>
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
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="账户" name="ACCOUNT">
        <div v-if="accountList.length === 0">
          <el-empty description="暂无数据"></el-empty>
        </div>
        <div v-else>
          <el-table
              :data="accountList"
              stripe
              style="width: 100%"
              :header-cell-style="{'text-align':'center'}"
              :cell-style="{'text-align':'center'}">
            <el-table-column
                prop="name"
                label="账户名称"
                width="500">
            </el-table-column>
            <el-table-column
                prop="amount"
                label="账户余额">
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="商品" name="PRODUCT">
        <div v-if="productList.length === 0">
          <el-empty description="暂无数据"></el-empty>
        </div>
        <div v-else>
          <el-table
              :data="productList"
              style="width: 100%;"
              :header-cell-style="{'text-align': 'center'}"
              :cell-style="{'text-align': 'center'}">
            <el-table-column
                prop="id"
                label="编号"
                width="200">
            </el-table-column>
            <el-table-column
                prop="name"
                label="名称"
                width="150">
            </el-table-column>
            <el-table-column
                prop="type"
                label="型号"
                width="300">
            </el-table-column>
            <el-table-column
                prop="quantity"
                label="库存数量"
                width="120">
            </el-table-column>
            <el-table-column
                prop="purchasePrice"
                label="进价"
                width="120">
            </el-table-column>
            <el-table-column
                prop="retailPrice"
                label="零售价"
                width="120">
            </el-table-column>
            <el-table-column
                prop="recentPp"
                label="最近进价"
                width="120">
            </el-table-column>
            <el-table-column
                prop="recentRp"
                label="最近零售价"
                width="120">
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>
  </Layout>
</template>

<script>
import Layout from "../../components/content/Layout";
import Title from "@/components/content/Title";

import { createBook, getBook, getId } from "../../network/finance";

export default {
  name: "Book",
  components: {
    Layout,
    Title,
  },
  data(){
    return{
      idList:[],
      book:{},
      id:'',
      productList:[],
      customerList:[],
      accountList:[],
    }
  },
  mounted() {
    getId().then(res=>{
      this.idList=res.result
    })
  },
  methods: {
    create(){
      createBook();
      location.reload();
      this.$message({
        type: 'success',
        message: '建账成功'
      })
    },
    getBookById(){
      console.log(this.id)
      let config = {
        params: {
          id: this.id
        }
      }
      getBook(config).then(res => {
        this.book=res.result
        this.customerList=this.book.customerList
        this.productList=this.book.productList
        this.accountList=this.book.accountList
      })
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
  }
}
</script>

<style scoped>

</style>
