<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-30 17:43:01
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-02 00:17:41
-->
<template>
  <div class="vip_strategy_list-warp">
    <div class="vip_strategy-head">
      <el-button @click="addVip" type="primary" size="small">新增权益</el-button>
    </div>
    <div class="content">
      <el-table height="85vh" :data="tableData" style="width: 100%">
        <el-table-column label="序号" type="index"></el-table-column>
        <el-table-column
          v-for="item in tableColumnList"
          :prop="item.prop"
          :label="item.label"
          :key="item.prop"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="() => editVip(scope.row)"
              >编辑</el-button
            >

            <el-button type="text" size="small" @click="() => deleteFn(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <addorEdit :getMemberRightsList="getMemberRightsList" ref="addorEditRef" />
  </div>
</template>

<script>
import { memberRightsList, memberRightsDelete } from "@/api/vipStrategyList";
import tableColumnList from "./tableColumnList";
import addorEdit from "./addorEdit/index.vue";
export default {
  data() {
    return {
      tableColumnList,
      tableData: [],
    };
  },
  components: {
    addorEdit,
  },

  methods: {
    deleteFn(data) {
      this.$confirm("此操作将永久删除会员权益, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        memberRightsDelete(data.id).then((res) => {
          if (res.code === 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getMemberRightsList();
          } else {
            this.$message({
              type: "error",
              message: res.message,
            });
          }
        });
      });
    },
    /**
     * @description: 获取会员列表
     * @return {*}
     * @author: jinglin.gao
     */
    async getMemberRightsList() {
      try {
        let res = await memberRightsList();
        if (res.code === 200) {
          this.tableData = res.result || [];
        }
      } catch (error) {
        console.log(error);
      }
    },

    /**
     * @description: 新增会员卡
     * @return {*}
     * @author: jinglin.gao
     */
    addVip() {
      this.$refs.addorEditRef.getPage();
    },

    // 编辑会员卡
    editVip(data) {
      this.$refs.addorEditRef.getPage(data);
    },
  },

  mounted() {
    this.getMemberRightsList();
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
