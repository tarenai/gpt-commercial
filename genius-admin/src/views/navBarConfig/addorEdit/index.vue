<template>
  <el-dialog
    :destroy-on-close="true"
    :title="rowData ? '编辑导航标题' : '新建导航标题'"
    :visible.sync="dialogVisible"
    width="550px"
  >
    <el-form ref="form" :rules="rules" :model="form" label-width="150px">
      <el-form-item label="导航标题名称" prop="buttonName">
        <el-input placeholder="请输入导航标题名称" v-model="form.buttonName"></el-input>
      </el-form-item>

      <el-form-item label="展示方式" prop="buttonType">
        <el-select
          style="width: 100%"
          placeholder="请选择展示方式"
          v-model="form.buttonType"
        >
          <el-option label="弹框展示" :value="10"></el-option>

          <el-option label="点击跳转" :value="20"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="导航描述内容" prop="buttonDesc">
        <el-input
          type="textarea"
          placeholder="请输入导航标题名称"
          v-model="form.buttonDesc"
        ></el-input>
      </el-form-item>

      <el-form-item label="展示图片地址" prop="imageUrl">
        <el-input placeholder="请输入展示图片地址" v-model="form.imageUrl"></el-input>
      </el-form-item>

      <el-form-item label="点击跳转地址" prop="jumpUrl">
        <el-input placeholder="请输入点击跳转地址" v-model="form.jumpUrl"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { topBarConfigSaveOrUpdate } from "@/api/navBarConfig";
export default {
  props: ["topBarConfigListFn"],
  data() {
    return {
      rowData: null,
      dialogVisible: false,
      form: {
        buttonName: "",
        buttonDesc: "",
        buttonType: "",
        imageUrl: "",
        jumpUrl: "",
      },
      rules: {
        buttonName: [{ required: true, message: "导航标题名称不能为空" }],
        buttonDesc: [{ required: false, message: "描述内容不能为空" }],
        buttonType: [{ required: true, message: "展示方式不能为空" }],
        imageUrl: [{ required: false, message: "展示图片地址不能为空" }],
        jumpUrl: [{ required: false, message: "跳转地址地址不能为空" }],
      },
    };
  },
  methods: {
    getPage(data) {
      this.dialogVisible = true;
      this.form = {
        buttonName: "",
        buttonDesc: "",
        buttonType: "",
        imageUrl: "",
        jumpUrl: "",
      };
      if (data) {
        this.rowData = data;
        this.form = JSON.parse(JSON.stringify(data));
      }
    },
    handleClose() {
      this.dialogVisible = false;
    },

    /**
     * @description: 新建导航描述
     * @return {*}
     * @author: jinglin.gao
     */
    async topBarConfigSaveOrUpdateFn() {
      try {
        let res = await topBarConfigSaveOrUpdate(this.form);
        if (res.code === 200) {
          this.$message({
            type: "success",
            message: "新建成功",
          });
          this.handleClose(); // 关闭窗口 或 保存配置项 按钮被点击后 执行此函数
          this.topBarConfigListFn();
        } else {
          this.$message({
            type: "error",
            message: res.message,
          });
        }
      } catch (error) {
        console.log(error);
      }
    },

    // 提交
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.topBarConfigSaveOrUpdateFn();
        } else {
          return false;
        }
      });
    },
  },
};
</script>
