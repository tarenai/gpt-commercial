<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-30 17:58:31
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-11 10:12:40
-->
<template>
  <div class="payment_order_list-warp">
    <div class="payment_order_list-head">
      <div class="searchBox">
        <el-input
          clearable
          style="width: 65%"
          v-model="paramsObj.userId"
          placeholder="请输入用户id"
        ></el-input>
        <el-button @click="getManagerOrdersList" style="margin-left: 10px" type="primary"
          >搜索</el-button
        >
      </div>
    </div>
    <el-table height="80vh" :data="tableData" style="width: 100%">
      <el-table-column label="序号" type="index"></el-table-column>
      <el-table-column
        v-for="item in tableColumnList"
        :prop="item.prop"
        :label="item.label"
        :key="item.prop"
      >
      </el-table-column>
    </el-table>

    <el-pagination
      style="float: right"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="paramsObj.current"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="paramsObj.size"
      layout="total, sizes, prev, pager, next"
      :total="paramsObj.total"
    >
    </el-pagination>
  </div>
</template>

<script>
import { managerOrders } from "@/api/paymentOrderList";
import tableColumnList from "./tableColumnList";
export default {
  data() {
    return {
      tableData: [],
      tableColumnList,
      paramsObj: {
        size: 10,
        current: 1,
        userId: "",
        total: 0,
      },
    };
  },
  methods: {
    // 获取订单列表
    async getManagerOrdersList() {
      try {
        let res = await managerOrders(this.paramsObj);
        if (res.code === 200) {
          const {
            result: { records, current, size, total },
          } = res;

          records.forEach((v) => {
            switch (v.payType) {
              case 10:
                v.payType = "微信支付";
                break;
              case 20:
                v.payType = "支付宝支付";
                break;
              default:
                break;
            }
          });
          this.tableData = records || []; //订单列表数组转换成列表形式展示给用户

          this.tableData.forEach((v) => {
            switch (v.payState) {
              case 0:
                v.payState = "支付中";
                break;
              case 1:
                v.payState = "支付成功";
                break;
              case 2:
                v.payState = "支付失败";
                break;
              case 3:
                v.payState = "支付取消";
                break;
              case 4:
                v.payState = "已退款";
                break;
              default:
                break;
            }
          });
          this.paramsObj.size = size;
          this.paramsObj.current = current;
          this.paramsObj.total = total; //记录总数计算条目数量，用于计算页面加载更多条目的
        }
      } catch (error) {
        console.log(error);
      }
    },

    // 每一条数该百年
    handleSizeChange(val) {
      this.paramsObj.size = val;
      this.getManagerOrdersList();
    },

    // 页数跳转
    handleCurrentChange(val) {
      this.paramsObj.current = val;
      this.getManagerOrdersList();
    },
  },

  mounted() {
    this.getManagerOrdersList();
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
