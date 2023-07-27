<template>
  <el-dialog
    :destroy-on-close="true"
    :title="rowData ? '编辑广告' : '新建广告'"
    :visible.sync="dialogVisible"
    width="550px"
  >
    <el-form ref="form" :rules="rules" :model="form" label-width="150px">
      <el-form-item label="广告名称" prop="advertiseName">
        <el-input placeholder="请输入广告名称" v-model="form.advertiseName"></el-input>
      </el-form-item>


      <el-form-item label="广告展示图片地址" prop="imgLink">
        <el-input placeholder="请输入广告展示图片地址" v-model="form.imgLink"></el-input>
      </el-form-item>

      
      <el-form-item label="广告地址" prop="advertiseLink">
        <el-input placeholder="请输入广告地址" v-model="form.advertiseLink"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { advertiseConfigSaveOrUpdate } from "@/api/bannerConfig";
export default {
  props: ["getAdvertiseList"],
  data() {
    return {
      rowData: null,
      dialogVisible: false,
      form: {
        advertiseName: "",
        advertiseLink: "",
        imgLink: "",
      },
      rules: {
        advertiseName: [{ required: true, message: "广告名称不能为空" }],
        advertiseLink: [{ required: false, message: "广告地址不能为空" }],
        imgLink: [{ required: true, message: "广告展示图片地址不能为空" }],
      },
    };
  },
  methods: {
    getPage(data) {
      this.dialogVisible = true;
      this.form = {
        advertiseName: "",
        advertiseLink: "",
        imgLink: "",
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
     * @description: 新建会员卡
     * @return {*}
     * @author: jinglin.gao
     */
    async advertiseConfigSaveOrUpdateFn() {
      try {
        let res = await advertiseConfigSaveOrUpdate(this.form);
        if (res.code === 200) {
          this.$message({
            type: "success",
            message: "新建成功",
          });
          this.handleClose(); // 关闭窗口 或 保存配置项 按钮被点击后 执行此函数
          this.getAdvertiseList();
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
          this.advertiseConfigSaveOrUpdateFn();
        } else {
          return false;
        }
      });
    },
  },
};
</script>
