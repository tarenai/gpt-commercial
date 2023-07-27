<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-30 17:58:31
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-21 17:17:37
-->
<template>
  <div class="userInfoList_list-warp">
    <div class="question_log__list-head">
      <div class="searchBox">
        <el-input
          class="searchBox_input"
          v-model="paramsObj.userId"
          placeholder="请输入用户id"
          clearable
        ></el-input>

        <el-input
          class="searchBox_input"
          v-model="paramsObj.phone"
          placeholder="请输入用户手机号"
          clearable
        ></el-input>
        <el-input
          class="searchBox_input"
          v-model="paramsObj.email"
          placeholder="请输入用户邮箱"
          clearable
        ></el-input>
        <el-button @click="getUserInfoList" style="margin-left: 10px" type="primary"
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

      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="() => vipRecharge(scope.row)"
            >充值</el-button
          >

          <el-button type="text" size="small" @click="() => memberExchangeFn(scope.row)"
            >用户充值详情</el-button
          >
        </template>
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

    <vipRecharge ref="vipRechargeRef" />
    <memberExchange ref="memberExchangeRef" />
  </div>
</template>

<script>
import { userInfoList } from "@/api/userInfoList";
import tableColumnList from "./tableColumnList";
import vipRecharge from "./vipRecharge/index.vue";
import memberExchange from "./memberExchange";
export default {
  data() {
    return {
      tableData: [],
      tableColumnList,
      paramsObj: {
        size: 10,
        current: 1,
        phone: "",
        email: "",
        userId: "",
      },
    };
  },
  components: {
    vipRecharge,
    memberExchange,
  },
  methods: {
    // 用户充值详情
    memberExchangeFn(data) {
      this.$refs.memberExchangeRef.getPage(data);
    },
    // 会员充值
    vipRecharge(data) {
      this.$refs.vipRechargeRef.getPage(data);
    },
    // 获取订单列表
    async getUserInfoList() {
      try {
        let res = await userInfoList(this.paramsObj);
        if (res.code === 200) {
          const {
            result: { records, current, size, total },
          } = res;
          records.forEach((v) => {
            switch (v.userStatus) {
              case 1:
                v.userStatus = "正常";
                break;
              case 2:
                v.userStatus = "注销";
                break;
              case 3:
                v.userStatus = "冻结";
                break;
              default:
                break;
            }
          });
          this.tableData = records || []; //订单列表数组转换成列表形式展示给用户
          this.tableData.forEach((v) => {
            switch (v.registerType) {
              case 1:
                v.registerType = "邮箱";
                break;
              case 2:
                v.registerType = "手机号";
                break;
              case 3:
                v.registerType = "微信授权登录";
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
      this.getUserInfoList();
    },

    // 页数跳转
    handleCurrentChange(val) {
      this.paramsObj.current = val;
      this.getUserInfoList();
    },
  },

  mounted() {
    this.getUserInfoList();
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
