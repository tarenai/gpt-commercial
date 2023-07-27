<template>
  <el-dialog
    :destroy-on-close="true"
    :title="rowData ? '编辑会员卡' : '新建会员卡'"
    :visible.sync="dialogVisible"
    width="550px"
  >
    <el-form ref="form" :rules="rules" :model="form" label-width="120px">
      <el-form-item label="会员卡编码" prop="cardCode">
        <el-input placeholder="请输入会员卡编码" v-model="form.cardCode"></el-input>
      </el-form-item>

      <el-form-item label="会员卡名" prop="cardName">
        <el-input placeholder="请输入会员卡名" v-model="form.cardName"></el-input>
      </el-form-item>

      <el-form-item label="卡状态" prop="cardState">
        <el-select
          placeholder="请选择卡状态"
          style="width: 100%"
          v-model="form.cardState"
        >
          <el-option label="上线" :value="1"></el-option>
          <el-option label="下线" :value="0"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="卡类型" prop="cardType">
        <el-select placeholder="请选择卡类型" style="width: 100%" v-model="form.cardType">
          <el-option label="普通类型" :value="1"></el-option>
          <el-option label="拉新赠送" :value="2"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="显示类型" prop="viewType">
        <el-select
          placeholder="请选择显示类型"
          style="width: 100%"
          v-model="form.viewType"
        >
          <el-option label="前端" :value="1"></el-option>
          <el-option label="后台" :value="2"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="卡金额" prop="amount">
        <el-input type="number" placeholder="请输入卡金额" v-model="form.amount"></el-input>
      </el-form-item>
      <el-form-item label="卡天数" prop="cardDay">
        <el-input placeholder="请输入卡天数" v-model="form.cardDay"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { memberConfigSaveOrUpdate } from "@/api/viplList";
export default {
  props: ["getMemberList"],
  data() {
    return {
      rowData: null,
      dialogVisible: false,
      form: {
        cardCode: "",
        cardName: "",
        cardState: "",
        cardType: "",
        viewType: "",
        amount: "",
        cardDay: "",
      },
      rules: {
        cardCode: [{ required: true, message: "会员卡编码" }],
        cardName: [{ required: true, message: "会员卡名" }],
        cardState: [{ required: true, message: "卡状态" }],
        cardType: [{ required: true, message: "卡类型" }],
        viewType: [{ required: true, message: "显示类型" }],
        amount: [{ required: true, message: "卡金额" }],
        cardDay: [{ required: true, message: "卡天数" }],
      },
    };
  },
  methods: {
    getPage(data) {
      console.log(data, "2222");
      this.dialogVisible = true;
      this.form = {
        cardCode: "",
        cardName: "",
        cardState: "",
        cardType: "",
        viewType: "",
        amount: "",
        cardDay: "",
      };
      if (data) {
        this.rowData = data;
        this.form = JSON.parse(JSON.stringify(data));

        if (this.form.cardState === "下线") {
          this.form.cardState = 0;
        } else if (this.form.cardState === "上线") {
          this.form.cardState = 1;
        }

        if (this.form.cardType === "普通类型") {
          this.form.cardType = 1;
        } else if (this.form.cardType === "拉新赠送") {
          this.form.cardType = 2;
        }

        if (this.form.viewType === "前端使用") {
          this.form.viewType = 1;
        } else if (this.form.viewType === "后台使用") {
          this.form.viewType = 2;
        }
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
    async memberConfigSaveOrUpdateFn() {
      try {
        let res = await memberConfigSaveOrUpdate(this.form);
        if (res.code === 200) {
          this.$message({
            type: "success",
            message: "新建会员卡成功",
          });
          this.handleClose(); // 关闭窗口 或 保存配置项 按钮被点击后 执行此函数
          this.getMemberList();
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
          this.memberConfigSaveOrUpdateFn();
        } else {
          return false;
        }
      });
    },
  },
};
</script>
