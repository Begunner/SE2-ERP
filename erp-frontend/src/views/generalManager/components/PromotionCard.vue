<template>
  <div class="card">
    <el-card v-for="(item, index) in list" :index="item.index" :key="item.id" shadow="hover">
      <template #header>
        <el-row>
          <el-col :span="8">
            <span><strong>id: </strong>{{item.id}}</span>
          </el-col>
          <el-col :span="8">
            <span><strong>开始时间: </strong>{{formatDate(item.beginTime)}}</span>
          </el-col>
          <el-col :span="8">
            <span><strong>结束时间: </strong>{{formatDate(item.endTime)}}</span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="12">
            <span><strong>促销条件: </strong></span>
          </el-col>
          <el-col :span="12">
            <span><strong>促销内容: </strong></span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <span><strong>客户最低级别: </strong>{{item.customerLevel===null?"无":item.customerLevel}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>最低总价: </strong>{{item.totalAmount===null?"无":item.totalAmount+"元"}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>折扣: </strong>{{item.discount===null?"无":item.discount}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>代金券: </strong>{{item.voucher===null?"无":item.voucher+"元"}}</span>
          </el-col>
        </el-row>
      </template>
      <div>
        <el-tabs active-name="PACKAGES" :stretch="true">
          <el-tab-pane label="促销所需购买商品" name="PACKAGES">
            <div>
              <el-table
                  :data="item.packages"
                  stripe
                  style="width: 100%"
                  :header-cell-style="{'text-align':'center'}"
                  :cell-style="{'text-align':'center'}">
                <el-table-column
                    prop="productId"
                    label="商品id"
                    width="300">
                </el-table-column>
                <el-table-column
                    prop="quantity"
                    label="数量">
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="赠品" name="GIFTS">
            <div>
              <el-table
                  :data="item.gifts"
                  stripe
                  style="width: 100%"
                  :header-cell-style="{'text-align':'center'}"
                  :cell-style="{'text-align':'center'}">
                <el-table-column
                    prop="productId"
                    label="商品id"
                    width="300">
                </el-table-column>
                <el-table-column
                    prop="quantity"
                    label="数量">
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>

export default {
  name: 'PromotionCard',
  props: {
    list: Array,
    type: Number
  },
  data() {
    return {

    }
  },
  methods: {
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
