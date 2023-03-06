<template>
  <Layout>
    <Title title="付款单"></Title>
    <el-button type="primary" size="medium" @click="dialogVisible = true">制定付款单</el-button>
    <div class="body">
      <el-tabs v-model="activeName" :stretch="true">
        <el-tab-pane label="待审批" name="PENDING">
          <div v-if="pendingList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <payment-card :list="pendingList" :type="1" @refresh="getPayment()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批完成" name="SUCCESS">
          <div v-if="successList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <payment-card :list="successList" :type="2"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批失败" name="FAILURE">
          <div v-if="failureList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <payment-card :list="failureList" :type="3"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog
        title="制定付款单"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose">
      <div style="width: 90%; margin: 0 auto">
        <el-form :model="paymentForm" label-width="100px" ref="paymentForm" :rules="rules">
          <el-form-item label="客户: " prop="supplier">
            <el-select v-model="paymentForm.supplier" placeholder="请选择客户">
              <el-option
                  v-for="item in customer"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item
              v-for="(item, index) in paymentForm.paymentSheetContent"
              :key="index"
              :label="'账户' + index">
            <div>
              <el-select v-model="item.account" placeholder="请选择账户" style="width: 40%; margin-right: 5%">
                <el-option
                    v-for="item1 in accountList"
                    :key="item1.id"
                    :label="item1.name"
                    :value="item1.name">
                </el-option>
              </el-select>
              <el-input v-model="item.amount" style="width: 50%; margin-right: 5%" placeholder="请输入转账金额"></el-input>
            </div>
            <div style="margin-top: 10px">
              <el-input v-model="item.remark" style="width: 80%; margin-right: 10%" placeholder="请填写备注"></el-input>
              <el-button type="text" size="small" @click="addAccount" v-if="index === paymentForm.paymentSheetContent.length - 1">添加</el-button>
              <el-button type="text" size="small" @click.prevent="removeAccount(item)" v-if="index !== 0">删除</el-button>
            </div>
          </el-form-item>
          <el-form-item label="备注: ">
            <el-input type="textarea" v-model="paymentForm.remark"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('paymentForm')">立即创建</el-button>
      </span>
    </el-dialog>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import PaymentCard from "./components/PaymentCard";
import { getAllPayment, createPayment } from '../../network/finance'
import { getAllCustomers } from '../../network/finance'
import { getAllAccounts } from '../../network/finance'

export default {
  name: 'PaymentView',
  components: {
    PaymentCard,
    Layout,
    Title
  },
  data() {
    return {
      activeName: 'PENDING',
      paymentList: [],
      pendingList: [],
      successList: [],
      failureList: [],
      dialogVisible: false,
      paymentForm: {
        paymentSheetContent: [
          {
            id: '',
            paymentSheetId: '',
            account: '',
            amount: '',
            remark: ''
          }
        ]
      },
      customer: [],
      rules: {
        supplier: [
          { required: true, message: '请选择一个客户', trigger: 'change' }
        ],
      },
      accountList: []
    }
  },
  mounted() {
    this.getPayment()
    getAllAccounts({}).then(_res => {
      this.accountList=_res.result
    })
    getAllCustomers().then(_res => {
      this.customer = _res.result
    })
  },
  methods: {
    getPayment() {
      getAllPayment({}).then(_res => {
        this.paymentList = _res.result
        this.pendingList = this.paymentList.filter(item => item.state === '待审批')
        this.successList = this.paymentList.filter(item => item.state === '审批完成')
        this.failureList = this.paymentList.filter(item => item.state === '审批失败')
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            this.resetForm()
            done();
          })
          .catch(_ => {});
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.paymentForm.id = null
          this.paymentForm.state = null
          this.paymentForm.createTime = null
          this.paymentForm.operator = null
          this.paymentForm.totalAmount = null
          this.paymentForm.paymentSheetContent.forEach((item) => {
            item.id = null
            item.paymentSheetId = null
            item.amount = Number(item.amount)
          })
          createPayment(this.paymentForm).then(_res => {
            if (_res.msg === 'Success') {
              this.$message.success('创建成功!')
              this.dialogVisible = false
              this.resetForm()
              this.getPayment()
            }
          })
        }
      })
    },
    resetForm() {
      this.paymentForm = {
        paymentSheetContent: [
          {
            id: '',
            paymentSheetId: '',
            account: '',
            amount: '',
            remark: ''
          }
        ]
      }
    },
    addAccount() {
      this.paymentForm.paymentSheetContent.push({});
    },
    removeAccount(item) {
      var index = this.paymentForm.paymentSheetContent.indexOf(item)
      if (index !== -1) {
        this.paymentForm.paymentSheetContent.splice(index, 1)
      }
    }
  }
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
