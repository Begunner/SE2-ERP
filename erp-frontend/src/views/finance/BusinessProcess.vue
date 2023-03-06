<template>
  <Layout>
    <Title title="经营历程表"></Title>
    <el-date-picker
        v-model="date"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="getFormsWithDate()">
    </el-date-picker>
    <el-button v-if="isFinding" @click="returnToAll" style="margin-left: 30px">返回所有列表</el-button>
    <el-button @click="excel" style="float: right">导出为EXCEL</el-button>
    <div class="body">

      <el-tabs v-model="activeName" :stretch="true">
        <el-tab-pane label="销售单" name="SALE">
          <div v-if="saleList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <sale-list :list="saleList" :type="3" @refresh="judgeAndRefresh()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="销售退货单" name="SALE_RETURN">
          <div v-if="saleReturnList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <sale-return-list :list="saleReturnList" :type="3" @refresh="judgeAndRefresh()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="进货单" name="PURCHASE">
          <div v-if="purchaseList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <purchase-list :list="purchaseList" :type="3" @refresh="judgeAndRefresh()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="进货退货单" name="PURCHASE_RETURN">
          <div v-if="purchaseReturnList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <purchase-return-list :list="purchaseReturnList" :type="3" @refresh="judgeAndRefresh()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="付款单" name="PAYMENT">
          <div v-if="paymentList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <payment-card :list="paymentList" :type="2" @refresh="judgeAndRefresh()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="收款单" name="RECEIPT">
          <div v-if="receiptList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <receipt-card :list="receiptList" :type="2" @refresh="judgeAndRefresh()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="工资单" name="SALARY">
          <div v-if="salaryList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-card :list="salaryList" :type="2" @refresh="judgeAndRefresh()"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import SaleList from '../sale/components/SaleList'
import SaleReturnList from "../sale/components/SaleReturnList";
import PurchaseList from "../purchase/components/PurchaseList";
import PurchaseReturnList from "../purchase/components/PurchaseReturnList";
import PaymentCard from "./components/PaymentCard";
import ReceiptCard from "./components/ReceiptCard";
import SalaryCard from "./components/SalaryCard";

import { getAllForms } from '../../network/finance'



export default {
  name: 'BusinessProcess',
  components: {
    Layout,
    Title,
    SaleList,
    SaleReturnList,
    PurchaseList,
    PurchaseReturnList,
    PaymentCard,
    ReceiptCard,
    SalaryCard,
  },
  data() {
    return {
      activeName: 'SALE',
      date:[],
      isFinding:false,
      allLists:[],

      saleList:[],
      saleReturnList:[],
      purchaseList:[],
      purchaseReturnList:[],
      paymentList:[],
      receiptList:[],
      salaryList:[],


    }
  },

  //用于获取格式化日期区间
  computed: {
    beginDate: function () {
      return this.date === '' ? '' : this.formatDate(this.date[0])
    },
    endDate: function () {
      return this.date === '' ? '' : this.formatDate(this.date[1])
    }
  },

  //加载页面时获取数据
  mounted() {
    this.getForms();
  },

  methods: {
    //获取所有单据后分类
    getForms() {
      getAllForms().then(_res => {
        if (_res.msg === "Error") {
          this.$message({
            type: "error",
            message: "获取失败！"
          });
        }
        else {
          this.allLists = _res.result
          this.saleList = this.allLists.filter(item => item.text === '销售单').map(item => {
            return item.value
          })
          this.saleReturnList = this.allLists.filter(item => item.text === '销售退货单').map(item => {
            return item.value
          })
          this.purchaseList = this.allLists.filter(item => item.text === '进货单').map(item => {
            return item.value
          })
          this.purchaseReturnList = this.allLists.filter(item => item.text === '进货退货单').map(item => {
            return item.value
          })
          this.paymentList = this.allLists.filter(item => item.text === '付款单').map(item => {
            return item.value
          })
          this.receiptList = this.allLists.filter(item => item.text === '收款单').map(item => {
            return item.value
          })
          this.salaryList = this.allLists.filter(item => item.text === '工资单').map(item => {
            return item.value
          })
        }
      })
    },
    //通过日期获取一个时间区间的单据
    getFormsWithDate() {
      const config = {
        params: {
          beginDateStr: this.beginDate,
          endDateStr: this.endDate
        }
      }
      getAllForms(config).then(_res => {
        if (_res.msg === "Error") {
          this.$message({
            type: "error",
            message: "获取失败！"
          });
        }
        else {
          this.allLists = _res.result
          this.saleList = this.allLists.filter(item => item.text === '销售单').map(item => {
            return item.value
          })
          this.saleReturnList = this.allLists.filter(item => item.text === '销售退货单').map(item => {
            return item.value
          })
          this.purchaseList = this.allLists.filter(item => item.text === '进货单').map(item => {
            return item.value
          })
          this.purchaseReturnList = this.allLists.filter(item => item.text === '进货退货单').map(item => {
            return item.value
          })
          this.paymentList = this.allLists.filter(item => item.text === '付款单').map(item => {
            return item.value
          })
          this.receiptList = this.allLists.filter(item => item.text === '收款单').map(item => {
            return item.value
          })
          this.salaryList = this.allLists.filter(item => item.text === '工资单').map(item => {
            return item.value
          })
          this.$message({
            type: "success",
            message: "筛选成功！"
          });
        }
      })
    },
    //用于从筛选中返回所有单据列表
    returnToAll() {
      this.getForms()
      this.isFinding=false
    },
    //根据当前是否在筛选中，进行相应的数据的更新
    judgeAndRefresh(){
      if(this.isFinding)
        this.getFormsWithDate()
      else
        this.getForms()
    },
    //防止用户不小心关闭
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_r => {
            this.resetForm()
            done();
          })
          .catch(_r => {});
    },
    //在列表中添加一个空项，供用户填入
    addProduct() {
      this.saleForm.saleSheetContent.push({});
    },
    //删除列表中的最后一项
    removeProduct(item) {
      var index = this.saleForm.saleSheetContent.indexOf(item)
      if (index !== -1) {
        this.saleForm.saleSheetContent.splice(index, 1)
      }
    },
    //日期格式化
    formatDate(gmt) {
      let date = new Date(gmt)
      let m = (date.getMonth() + 1), mm = '-' + (m < 10 ? '0' + m : m)
      let d = date.getDate(), dd = '-' + (d < 10 ? '0' + d : d)
      let h = date.getHours(), hh = ' ' +(h < 10 ? '0' + h : h)
      let i = date.getMinutes(), ii = ':' +(i < 10 ? '0' + i : i)
      let s = date.getSeconds(), ss = ':' +(s < 10 ? '0' + s : s)
      return date.getFullYear() + mm + dd + hh + ii + ss
    },
  },





}
</script>

<style lang="scss" scoped>
.body {
  margin: 0 auto;
  margin-top: 10px;
  height: 80vh;
  overflow-y: auto;
  width: 100%;
  background: rgba($color: #fff, $alpha: 0.5);
}
</style>
