<template>
  <Layout>
    <Title title="销售明细表"></Title>
    <div>
      <el-date-picker
          v-model="date"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="getData()">
      </el-date-picker>
      <el-button v-if="isFinding" @click="returnToAll" style="margin-left: 30px">返回所有列表</el-button>
      <el-button @click="excel" style="float: right">导出为EXCEL</el-button>
      <div class="mt15" v-if="!isFinding">
        <el-table
            :data="saleDetailList"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}"
            class="mt15">
          <el-table-column
              prop="type"
              label="类型"
              width="100"
              :filters="[{ text: '销售单', value: '销售' }, { text: '销售退货单', value: '销售退货' }]"
              :filter-method="filterType"
              filter-placement="bottom-end">
          </el-table-column>
          <el-table-column
              width="200"
              label="日期">
            <template slot-scope="scope">
              {{ formatDate(scope.row.date) }}
            </template>
          </el-table-column>
          <el-table-column
              prop="supplier"
              label="客户id"
              width="100"
              :filters="customerList"
              :filter-method="filterCustomer"
              filter-placement="bottom-end">
          </el-table-column>
          <el-table-column
              prop="salesman"
              label="销售员"
              width="180"
              :filters="salesmanList"
              :filter-method="filterSalesman"
              filter-placement="bottom-end">
          </el-table-column>
          <el-table-column
              prop="productId"
              label="商品id"
              width="180">
          </el-table-column>
          <el-table-column
              prop="productName"
              label="名称"
              width="180"
              :filters="productList"
              :filter-method="filterProduct"
              filter-placement="bottom-end">
          </el-table-column>
          <el-table-column
              prop="productType"
              label="型号"
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
              label="总价(元)"
              width="200">
          </el-table-column>
        </el-table>
      </div>
<!--      同步修改以下片段-->
      <div class="mt15" v-if="isFinding">
        <el-table
            :data="findSaleDetailList"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}"
            class="mt15">
          <el-table-column
              prop="type"
              label="类型"
              width="100"
              :filters="[{ text: '销售单', value: '销售' }, { text: '销售退货单', value: '销售退货' }]"
              :filter-method="filterType"
              filter-placement="bottom-end">
          </el-table-column>
          <el-table-column
              width="500"
              label="日期">
            <template slot-scope="scope">
              {{ formatDate(scope.row.date) }}
            </template>
          </el-table-column>
          <el-table-column
              prop="supplier"
              label="客户id"
              width="180"
              :filters="customerList"
              :filter-method="filterCustomer"
              filter-placement="bottom-end">>
          </el-table-column>
          <el-table-column
              prop="salesman"
              label="销售员"
              width="180"
              :filters="salesmanList"
              :filter-method="filterSalesman"
              filter-placement="bottom-end">>
          </el-table-column>
          <el-table-column
              prop="productId"
              label="商品id"
              width="180">
          </el-table-column>
          <el-table-column
              prop="productName"
              label="名称"
              width="180"
              :filters="productList"
              :filter-method="filterProduct"
              filter-placement="bottom-end">>
          </el-table-column>
          <el-table-column
              prop="productType"
              label="型号"
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
              label="总价(元)"
              width="200">
          </el-table-column>
        </el-table>
      </div>
    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import { getSaleDetail, getProduct, getCustomer, getSalesman, excelSaleDetail } from "../../network/finance"

export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      date: [],  //刚刚获取的数据
      saleDetailList: [],  //
      findSaleDetailList: [],
      productList:[],
      customerList:[],
      salesmanList:[],
      isFinding: false,
    }
  },
  computed: {
    beginDate: function () {
      return this.date === '' ? '' : this.formatDate(this.date[0])
    },
    endDate: function () {
      return this.date === '' ? '' : this.formatDate(this.date[1])
    }
  },
  async mounted() {
    await getProduct().then(_res => {
      this.productList = _res.result
      getSalesman().then(_res => {
        this.salesmanList = _res.result
        getCustomer().then(_res => {
          this.customerList = _res.result
          getSaleDetail().then(_res => {
            this.saleDetailList = _res.result
          })
        })
      })
    })
  },
  methods: {
    filterType(value, row) {
      return row.type === value
    },
    filterSalesman(value, row) {
      return row.salesman === value
    },
    filterCustomer(value, row) {
      return row.supplier === value
    },
    filterProduct(value, row) {
      return row.productName === value
    },
    getData() {
      this.findSaleDetailList = []
      const config = {
        params: {
          beginDateStr: this.beginDate,
          endDateStr: this.endDate
        }
      }
      getSaleDetail(config).then(_res => {
        this.findSaleDetailList = _res.result
        this.$message.success('查询成功!')
      })
      this.isFinding = true
    },
    returnToAll() {
      this.isFinding = false
      getSaleDetail().then(_res => {
        this.saleDetailList = _res.result
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
    excel(){
      excelSaleDetail({
        responseType:'blob',
        headers:{
          'Content-Type': 'application/x-download'
        }
      }).then(res =>{
        const link = document.createElement('a');
        let blob = new Blob([res.data],{type:'application/vnd.ms-excel'});
        const fileName = "销售明细表.xlsx";
        link.href = URL.createObjectURL(blob);
        link.download = fileName;
        document.body.appendChild(link);
        link.click();
        URL.revokeObjectURL(link.href);
        document.body.removeChild(link);

        // link.style.display="none";
        // link.href=URL.createObjectURL(res);
        // link.setAttribute("download","销售明细表.xlsx");
        // document.body.appendChild(link);
        // link.click();
        // document.body.removeChild(link);
      })

    }
  }
};
</script>

<style scoped lang="scss">
.mt15 {
  margin-top: 15px;
}
</style>
