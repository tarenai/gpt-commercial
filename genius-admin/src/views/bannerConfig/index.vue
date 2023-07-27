<template>
  <div class="page_warp">
    <div class="page_list-head">
      <el-button @click="addAdv" type="primary" size="small">新增广告</el-button>
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
            <el-button type="text" size="small" @click="() => editAdv(scope.row)"
              >编辑</el-button
            >

            <el-button
              type="text"
              size="small"
              @click="() => deleteAdvertiseFn(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <addorEdit :getAdvertiseList="getAdvertiseList" ref="addorEditRef" />
  </div>
</template>

<script>
import {
  advertiseList,
  deleteAdvertise,

} from "@/api/bannerConfig";
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
    // 删除vip
    deleteAdvertiseFn(data) {
      this.$confirm("此操作将永久删除配置, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deleteAdvertise(data.id).then((res) => {
          if (res.code === 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getAdvertiseList();
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
    async getAdvertiseList() {
      try {
        let res = await advertiseList();
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
    addAdv() {
      this.$refs.addorEditRef.getPage();
    },

    // 编辑会员卡
    editAdv(data) {
      this.$refs.addorEditRef.getPage(data);
    },
  },

  mounted() {
    this.getAdvertiseList();
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
