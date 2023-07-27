<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-06 13:15:01
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-21 17:37:10
-->
<template>
  <el-dialog
    :destroy-on-close="true"
    title="用户充值详情"
    :visible.sync="dialogVisible"
    width="650px"
  >
    <el-table height="350px" :data="tableData" style="width: 100%">
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
  </el-dialog>
</template>

<script>
import { memberExchangeFn } from "@/api/userInfoList";
import { memberList } from "@/api/viplList";
export default {
  data() {
    return {
      dialogVisible: false,
      tableData: [],
      tableColumnList: [
        {
          prop: "memberCardName",
          label: "会员卡名称",
        },
        {
          prop: "createTime",
          label: "充值时间",
        },

        {
          prop: "expiresTime",
          label: "过期时间",
        },

        {
          prop: "totalCount",
          label: "总次数",
        },
        {
          prop: "surplusCount",
          label: "剩余次数",
        },
      ],
      paramsObj: {
        size: 9999,
        current: 1,
        userId: "",
      },
      rowData: {},
      memberListData: [],
    };
  },

  methods: {
    // 获取会员列表
    async getMemberList() {
      try {
        let res = await memberList();
        if (res.code === 200) {
          this.memberListData = res.result || [];
        }
      } catch (error) {
        console.log(error);
      }
    },
    // 每一条数该百年
    handleSizeChange(val) {
      this.paramsObj.size = val;
      this.memberExchange();
    },

    // 页数跳转
    handleCurrentChange(val) {
      this.paramsObj.current = val;
      this.memberExchange();
    },

    // 获取会员卡权益
    async memberExchange() {
      try {
        let res = await memberExchangeFn(this.paramsObj);
        if (res.code === 200) {
          let resData = res.result?.records || [];
          resData.forEach((v) => {
            let activeItem = this.memberListData.find(
              (item) => item.id === v.memberCardId
            );

            v.memberCardName = activeItem?.cardName || "";
          });
          this.tableData = resData;
        }
      } catch (error) {
        console.log(error);
      }
    },

    async getPage(data) {
      this.dialogVisible = true;
      this.rowData = data;
      this.paramsObj.userId = data.id; // 设置参数值 供选择器使用 不要改变参数值后
      await this.getMemberList();
      await this.memberExchange();
    },
    handleClose() {
      this.dialogVisible = false;
    },
  },
};
</script>
