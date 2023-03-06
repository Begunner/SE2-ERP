<template>
  <Layout>
    <Title title="促销策略"></Title>
    <el-button style="margin-bottom: 15px" v-if="authorization() === 1" type="primary" size="medium" @click="create">创建促销策略</el-button>
    <div v-if="promotionList.length === 0">
      <el-empty description="暂无数据"></el-empty>
    </div>
    <div v-else>
      <promotion-card :list="promotionList"/>
    </div>
    <el-dialog
        title="填写促销策略"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose">
      <div style="width: 90%; margin: 0 auto">
        <el-form :model="promotionSheet" label-width="100px" ref="promotionSheet">
          <el-form-item label="促销时间: " prop="date">
            <el-date-picker
                v-model="date"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                @change="setDate()">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="客户等级: ">
            <el-select name="level" v-model="promotionSheet.customerLevel">
              <el-option value="1">一级</el-option>
              <el-option value="2">二级</el-option>
              <el-option value="3">三级</el-option>
              <el-option value="4">四级</el-option>
              <el-option value="5">五级</el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="最低总价: " prop="lowLimit">
            <el-input v-model="promotionSheet.totalAmount" style="width: 70%; margin-right: 10%" placeholder="请填写最低总价"></el-input>
          </el-form-item>
          <el-form-item
              v-for="(item, index) in packages"
              :key="index"
              :label="'促销商品' + index">
            <div>
              <el-select v-model="item.productId" placeholder="请选择商品id" style="width: 40%; margin-right: 5%">
                <el-option
                    v-for="item1 in commodityList"
                    :key="item1.id"
                    :label="item1.id"
                    :value="item1.id">
                </el-option>
              </el-select>
              <el-input v-model="item.quantity" style="width: 25%; margin-right: 5%" placeholder="请输入商品数量"></el-input>
            </div>
            <div style="margin-top: 10px">
              <el-button type="text" size="small" @click="addPackage" v-if="index === packages.length - 1">添加</el-button>
              <el-button type="text" size="small" @click.prevent="removePackage(item)" v-if="index !== 0">删除</el-button>
            </div>
          </el-form-item>
          <el-form-item label="促销折扣: ">
            <el-input v-model="promotionSheet.discount" style="width: 70%; margin-right: 10%" placeholder="请填写折扣"></el-input>
          </el-form-item>
          <el-form-item label="代金券: " prop="voucher">
            <el-input v-model="promotionSheet.voucher" style="width: 70%; margin-right: 10%" placeholder="请填写提供的代金券"></el-input>
          </el-form-item>
          <el-form-item
              v-for="(item, index) in gifts"
              :key="index"
              :label="'赠品' + index">
            <div>
              <el-select v-model="item.productId" placeholder="请选择商品id" style="width: 40%; margin-right: 5%">
                <el-option
                    v-for="item1 in commodityList"
                    :key="item1.id"
                    :label="item1.id"
                    :value="item1.id">
                </el-option>
              </el-select>
              <el-input v-model="item.quantity" style="width: 25%; margin-right: 5%" placeholder="请输入商品数量"></el-input>
            </div>
            <div style="margin-top: 10px">
              <el-button type="text" size="small" @click="addGift" v-if="index === gifts.length - 1">添加</el-button>
              <el-button type="text" size="small" @click.prevent="removeGift(item)" v-if="index !== 0">删除</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm()">立即提交</el-button>
      </span>
    </el-dialog>
  </Layout>

</template>

<script>
import Layout from "../../components/content/Layout";
import Title from "../../components/content/Title";
import PromotionCard from "./components/PromotionCard";
import {createPromotion, getAllPromotion} from "../../network/generalManager";
import {getAllCommodity} from "../../network/commodity";


export default {
  name: "Promotion",
  components: {
    PromotionCard,
    Layout,
    Title,
  },
  data() {
    return {
      date: [],
      promotionList: [],
      dialogVisible: false,
      promotionSheet: {
        customerLevel:'',
        totalAmount:'',
        discount:'',
        voucher:'',
      },
      commodityList: [],
      packages:[{
        productId:'',
        quantity:'',
        }],
      gifts:[{
        productId:'',
        quantity:'',
      }],

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

  //加载页面时获取所有前置数据
  mounted() {
    getAllPromotion({}).then(_res => {
      this.promotionList = _res.result
    })
    getAllCommodity({}).then(_res => {
      let res = _res.result
      res.forEach(item => this.commodityList.push({ id: item.id }))
    })
  },
  methods: {
    //验证身份，销售类人员没有新增权限，ADMIN与总经理有相应权限
    authorization() {
      if (sessionStorage.getItem('role') === 'GM' || sessionStorage.getItem('role') === 'ADMIN') {
        return 1
      } else if (sessionStorage.getItem('role') === 'SALE_MANAGER' || sessionStorage.getItem('role') === 'SALE_STAFF') {
        return 2
      }
    },
    //用于判断商品列表中是否有非法数据，这里是判断数量为负数
    judgePositive(){
      for(let i=0;i<this.packages.length;i++){
        if(this.packages[i].quantity<0){
          return false
        }
      }
      for(let i=0;i<this.gifts.length;i++){
        if(this.gifts[i].quantity<0){
          return false
        }
      }
      return true
    },
    //将日期填入表单
    setDate(){
      this.promotionSheet.beginTime = this.beginDate
      this.promotionSheet.endTime = this.endDate
    },
    //用于唤出表单页面
    create(){
      this.resetForm()
      this.dialogVisible=true
    },
    //防止用户不小心关闭
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            this.resetForm()
            done();
          })
          .catch(_ => {});
    },
    //提交单据
    submitForm() {
      if(this.promotionSheet.discount>1||this.promotionSheet.discount<0)
        alert("折扣必须在0~1之间")
      else if(this.promotionSheet.totalAmount!==''&&this.promotionSheet.totalAmount<0)
        alert("最低总价不能为负数")
      else if(this.promotionSheet.voucher!==''&&this.promotionSheet.voucher<0)
        alert("代金券不能为负数")
      else if(!this.judgePositive()){
        alert("数量不能为负数")
      }
      else {
        this.promotionSheet.packages = this.packages === [{productId:'', quantity:''}]?[]:this.packages
        this.promotionSheet.gifts = this.gifts === [{productId:'', quantity:''}]?[]:this.gifts
        createPromotion(this.promotionSheet).then(res => {
          if(res.code==="Z0001"){
            this.$message({
              type: "error",
              message: "创建失败，请正确填写日期！"
            });
          }else if(res.code==="Z0003"){
            this.$message({
              type: "error",
              message: "创建失败，请填写商品与数量！"
            });
          }
          else if(res.code==="Z0004"){
            this.$message({
              type: "error",
              message: "创建失败，请选择促销优惠！"
            });
          }
          else{
            this.$message({
              type: "success",
              message: "创建成功!"
            });
            this.dialogVisible = false
            getAllPromotion({}).then(_res => {
              this.promotionList = _res.result
            })
          }
        })
      }
    },
    resetForm() {
      this.promotionSheet = {
          customerLevel:'',
          totalAmount:'',
          discount:'',
          voucher:'',
      }
      this.packages=[{
        productId:'',
        quantity:'',
      }]
      this.gifts=[{
        productId:'',
        quantity:'',
      }]
      this.date=[]
    },
    addPackage() {
      this.packages.push({});
    },
    removePackage(item) {
      var index = this.packages.indexOf(item)
      if (index !== -1) {
        this.packages.splice(index, 1)
      }
    },
    addGift() {
      this.gifts.push({});
    },
    removeGift(item) {
      var index = this.gifts.indexOf(item)
      if (index !== -1) {
        this.gifts.splice(index, 1)
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

