<template>
  <div class="vip_list_warp">
    <div class="vip_list-head">
      <el-button @click="addVip" type="primary" size="small">新增会员卡</el-button>
      
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

            <el-button type="text" size="small" @click="() => deleteVip(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <addorEdit :getMemberList="getMemberList" ref="addorEditRef" />

  </div>
</template>

<script>
import { memberList, memberConfigDelete } from "@/api/viplList";
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
    deleteVip(data) {
      this.$confirm("此操作将永久删除会员配置, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        memberConfigDelete(data.id).then((res) => {
          if (res.code === 200) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getMemberList();
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
    async getMemberList() {
      try {
        let res = await memberList();
        if (res.code === 200) {
          this.tableData = res.result || [];
          this.tableData.forEach((v) => {
            if (v.cardState === 0) {
              v.cardState = "下线";
            } else if (v.cardState === 1) {
              v.cardState = "上线";
            }

            if (v.cardType === 1) {
              v.cardType = "普通类型";
            } else if (v.cardType === 2) {
              v.cardType = "拉新赠送";
            }

            if (v.viewType === 1) {
              v.viewType = "前端使用";
            } else if (v.viewType === 2) {
              v.viewType = "后台使用";
            }
          });
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
    this.getMemberList();
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
