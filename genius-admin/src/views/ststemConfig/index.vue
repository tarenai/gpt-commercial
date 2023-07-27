<template>
  <div class="ststem_config-warp">
    <el-tabs v-model="activeName">
      <el-tab-pane label="网站配置" name="0"></el-tab-pane>
    </el-tabs>

    <div class="ststem_config-content">
      <!-- 网站配置 -->
      <div class="ststem_config-box" v-show="activeName === '0'">
        <el-form
          :model="sysForm"
          :rules="sysRules"
          ref="sysFormRef"
          label-width="150px"
          class="sys-ruleForm"
        >
          <el-form-item label="系统名称" prop="webName">
            <el-input placeholder="请填写系统名称" v-model="sysForm.webName"></el-input>
          </el-form-item>

          <el-form-item label="登录页欢迎语" prop="subTitle">
            <el-input
              placeholder="请填写登录页欢迎语"
              v-model="sysForm.subTitle"
            ></el-input>
          </el-form-item>

          <el-form-item label="网站Logo" prop="iconUrl">
            <el-input
              placeholder="请填写网站Logo地址"
              v-model="sysForm.iconUrl"
            ></el-input>
          </el-form-item>

          <el-form-item label="互联网公安备案号" prop="filingNumber">
            <el-input
              placeholder="请填写互联网公安备案号"
              v-model="sysForm.filingNumber"
            ></el-input>
          </el-form-item>

          <el-form-item label="登录方式" prop="loginType">
            <el-radio v-model="sysForm.loginType" :label="3">微信授权注册登录</el-radio>
            <el-radio v-model="sysForm.loginType" :label="1">邮箱注册登录</el-radio>
            <el-radio v-model="sysForm.loginType" :label="2">阿里短信验证码注册登录</el-radio>
          </el-form-item>

          <p class="info">勾选前请确保在yaml配置文件中有相应的配置</p>

          <el-form-item style="text-align: center">
            <el-button type="primary" @click="onSysFormSubmit">保存配置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { saveSetting, getSetting } from "@/api/systemConfig";
export default {
  data() {
    return {
      activeName: "0",
      sysForm: {
        webName: "",
        iconUrl: "",
        subTitle: "",
        filingNumber: "",
        loginType: 1,
      },
      sysRules: {
        webName: [{ required: true, message: "请输入系统名称" }],
        iconUrl: [
          {
            required: true,
            message: "请输入网站Logo地址",
          },
        ],
        subTitle: [
          {
            required: true,
            message: "请输入登录页欢迎语",
          },
        ],
        loginType: [
          {
            required: true,
            message: "请选择网站登录方式",
          },
        ],
        filingNumber: [
          {
            required: false,
            message: "请选择网站登录方式",
          },
        ],
      },
    };
  },

  mounted() {
    // 获取系统配置
    this.getSettingFn();
  },
  methods: {
    // 获取系统配置
    async getSettingFn() {
      try {
        let res = await getSetting();
        if (res.code === 200) {
          this.sysForm = res.result;
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

    // 网站配置保存
    onSysFormSubmit() {
      // saveSetting
      this.$refs.sysFormRef.validate((valid) => {
        if (valid) {
          this.saveSettingFn();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },

    // 网站配置保存
    async saveSettingFn() {
      try {
        let res = await saveSetting(this.sysForm);
        if (res.code === 200) {
          this.$message({
            type: "success",
            message: "配置保存成功！",
          });
        } else {
          this.$message({
            type: "error",
            message: res.msg,
          });
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
