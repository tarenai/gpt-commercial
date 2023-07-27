<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-04 16:29:05
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-24 08:43:59
-->
<template>
  <div class="dashboard">
    <div class="dashboard_warp">
      <div class="dashboard_warp-top">
        <div class="dashboard_title">数据统计</div>
        <div class="dashboard_head-warp">
          <div class="dashboard_head-item">
            <div class="dashboard_item-head">
              <img class="item-img" src="@/assets/dashborad/user.png" alt="" />
              今日新增用户
            </div>
            <div class="dashboard_item-num">{{ registerUserTrendCount }} 人</div>
          </div>
          <div class="dashboard_head-item">
            <div class="dashboard_item-head">
              <img class="item-img" src="@/assets/dashborad/trend.png" alt="" />
              今日用户问答数
            </div>
            <div class="dashboard_item-num">{{ qATrendCount }} 次</div>
          </div>

          <div class="dashboard_head-item">
            <div class="dashboard_item-head">
              <img class="item-img" src="@/assets/dashborad/order.png" alt="" />
              今日用户支付成功数
            </div>
            <div class="dashboard_item-num">{{ userPaymentCount }} 次</div>
          </div>

          <div class="dashboard_head-item">
            <div class="dashboard_item-head">
              <img class="item-img" src="@/assets/dashborad/order.png" alt="" />
              今日成功支付金额
            </div>
            <div class="dashboard_item-num">{{ paymentSuccessCount }} 元</div>
          </div>
        </div>
      </div>

      <div class="dashboard_content-warp">
        <div class="dashboard_title">数据详情</div>

        <div class="dashboard_chat-box">
          <div class="dashboard_chat-item">
            <div class="chart_item-head">
              <div class="chart_item-head-title">注册用户趋势</div>

              <div class="chart_item-tabs">
                <el-radio-group size="small" v-model="registerUserTrendFilter">
                  <el-radio-button :label="7">7天</el-radio-button>
                  <el-radio-button :label="15">15天</el-radio-button>
                </el-radio-group>
              </div>
            </div>

            <div class="chat-box">
              <CustomEchart :option="registerUserTrendOption" />
            </div>
          </div>

          <div class="dashboard_chat-item">
            <div class="chart_item-head">
              <div class="chart_item-head-title">用户提问趋势图</div>

              <div class="chart_item-tabs">
                <el-radio-group size="small" v-model="qATrendFilter">
                  <el-radio-button :label="7">7天</el-radio-button>
                  <el-radio-button :label="15">15天</el-radio-button>
                </el-radio-group>
              </div>
            </div>

            <div class="chat-box">
              <CustomEchart :option="qATrendOption" />
            </div>
          </div>
        </div>

        <div class="dashboard_chat-box">
          <div class="dashboard_chat-item">
            <div class="chart_item-head">
              <div class="chart_item-head-title">用户支付趋势图</div>

              <div class="chart_item-tabs">
                <el-radio-group size="small" v-model="userPaymentFilter">
                  <el-radio-button :label="7">7天</el-radio-button>
                  <el-radio-button :label="15">15天</el-radio-button>
                </el-radio-group>

                <el-radio-group
                  size="small"
                  style="margin-left: 20px"
                  v-model="userPaymentState"
                >
                  <el-radio-button :label="0">支付中</el-radio-button>
                  <el-radio-button :label="1">支付成功</el-radio-button>
                  <el-radio-button :label="2">支付失败</el-radio-button>
                  <el-radio-button :label="3">支付取消</el-radio-button>
                </el-radio-group>
              </div>
            </div>

            <div class="chat-box">
              <CustomEchart :option="userPaymentOption" />
            </div>
          </div>

          <div class="dashboard_chat-item">
            <div class="chart_item-head">
              <div class="chart_item-head-title">成功支付金额趋势图</div>

              <div class="chart_item-tabs">
                <el-radio-group size="small" v-model="paymentSuccessfulFilter">
                  <el-radio-button :label="7">7天</el-radio-button>
                  <el-radio-button :label="15">15天</el-radio-button>
                </el-radio-group>
              </div>
            </div>

            <div class="chat-box">
              <CustomEchart :option="paymentSuccessfulOption" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { registerUserTrend, qATrend, payTrend, payAmountTrend } from "@/api/dashboard.js";
import CustomEchart from "@/components/CustomEchart";
import {
  registerUserTrendOption,
  qATrendOption,
  userPaymentOption,
  paymentSuccessfulOption,
} from "./options/index";
export default {
  name: "Dashboard",
  components: {
    CustomEchart,
  },
  data() {
    return {
      // 注册用户趋势图
      registerUserTrendOption,
      registerUserTrendFilter: 7,
      registerUserTrendCount: 0,

      // 用户提问趋势图
      qATrendOption,
      qATrendFilter: 7,
      qATrendCount: 0,

      // 用户支付趋势图
      userPaymentOption,
      userPaymentFilter: 7, //7天或以上天数记录展示用户支付趋势图的条目数量，
      userPaymentCount: 0, //记录数量。
      userPaymentState: 1, //0失败  1成功

      // 成功支付金额趋势图
      paymentSuccessfulOption,
      paymentSuccessfulFilter: 7,
      paymentSuccessCount: 0,
    };
  },
  watch: {
    // 注册用户趋势图
    registerUserTrendFilter(value) {
      this.registerUserTrendFn(value);
    },

    // 用户提问趋势图
    qATrendFilter(value) {
      this.qATrendFn(value);
    },

    // 用户支付趋势图
    userPaymentFilter() {
      this.payTrendFn(this.userPaymentFilter, this.userPaymentState);
    },

    // 用户支付趋势图
    userPaymentState() {
      this.payTrendFn(this.userPaymentFilter, this.userPaymentState);
    },

    // 成功支付金额趋势图
    paymentSuccessfulFilter() {
      this.payAmountTrendFn(this.paymentSuccessfulFilter);
    },
  },

  mounted() {
    this.initData();
  },
  methods: {
    async initData() {
      // 查询今日的用户注册趋势
      await this.registerUserTrendFn(0);
      // 注册用户趋势
      await this.registerUserTrendFn(this.registerUserTrendFilter);

      // 查询今日用户提问次数
      await this.qATrendFn(0);
      // 用户提问趋势图
      await this.qATrendFn(this.userPaymentFilter);

      // 用户支付成功趋势图
      await this.payTrendFn(0, 1);
      // 今日支付次数
      await this.payTrendFn(this.userPaymentFilter, this.userPaymentState);

      // 今日成功支付金额
      await this.payAmountTrendFn(0);

      // 今日成功支付金额趋势图
      await this.payAmountTrendFn(this.paymentSuccessfulFilter);
    },

    /**
     * @description: 成功支付金额趋势图
     * @return {*}day
     * @author: jinglin.gao
     */
    async payAmountTrendFn(day) {
      try {
        let params = {
          day,
        };
        let res = await payAmountTrend(params);
        if (res.code === 200) {
          if (day === 0) {
            this.paymentSuccessCount = res.result.length ? res.result[0].count : 0;
          } else {
            let resData = res.result || [];
            this.paymentSuccessfulOption.xAxis.data = resData.map((v) => v.date);
            this.paymentSuccessfulOption.series[0].data = resData.map((v) =>
              Number(v.count)
            );
          }
        }
      } catch (error) {
        console.log(error);
      }
    },

    /**
     * @description: 获取注册用户趋势图
     * @return {*}
     * @author: jinglin.gao
     */
    async registerUserTrendFn(day) {
      try {
        let params = {
          day,
        };
        let res = await registerUserTrend(params);
        if (res.code === 200) {
          if (day === 0) {
            this.registerUserTrendCount = res.result.length ? res.result[0].count : 0;
          } else {
            let resData = res.result || [];
            this.registerUserTrendOption.xAxis.data = resData.map((v) => v.date);
            this.registerUserTrendOption.series[0].data = resData.map((v) =>
              Number(v.count)
            );
          }
        }
      } catch (error) {
        console.log(error);
      }
    },

    // 用户提问趋势图
    async qATrendFn(day) {
      try {
        let params = {
          day,
        };
        let res = await qATrend(params);
        if (res.code === 200) {
          if (day === 0) {
            this.qATrendCount = res.result.length ? res.result[0].count : 0;
          } else {
            let resData = res.result || [];
            this.qATrendOption.xAxis.data = resData.map((v) => v.date);
            this.qATrendOption.series[0].data = resData.map((v) => Number(v.count));
          }
        }
      } catch (error) {
        console.log(error);
      }
    },

    //用户支付趋势图
    async payTrendFn(day, payState = 1) {
      try {
        let params = {
          day,
          payState,
        };
        let res = await payTrend(params);
        if (res.code === 200) {
          if (day === 0) {
            this.userPaymentCount = res.result.length ? res.result[0].count : 0;
          } else {
            let resData = res.result || [];
            this.userPaymentOption.xAxis.data = resData.map((v) => v.date);
            this.userPaymentOption.series[0].data = resData.map((v) => Number(v.count));
          }
        }
      } catch (error) {
        console.log(error);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
