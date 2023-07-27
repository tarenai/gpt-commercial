<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-02 00:56:26
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-02 01:00:07
-->
<template>
  <el-dialog
    :destroy-on-close="true"
    title="消息详情"
    :visible.sync="dialogVisible"
    width="550px"
  >
    <el-input type="textarea" resize="none" rows="20" readonly="" v-model="textarea"> </el-input>
  </el-dialog>
</template>

<script>
export default {
  props: ["getMemberList"],
  data() {
    return {
      dialogVisible: false,
      textarea: "",
    };
  },
  methods: {
    getPage(data) {
      this.dialogVisible = true;
      this.textarea = data.content;
    },
    handleClose() {
      this.dialogVisible = false;
    },
  },
};
</script>
