<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-06 13:15:01
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-07 21:24:29
-->
<template>
  <el-dialog
    :destroy-on-close="true"
    title="会员充值"
    :visible.sync="dialogVisible"
    width="550px"
  >
    <el-form ref="form" :rules="rules" :model="form" label-width="150px">
      <el-form-item label="会员卡权益" prop="memberCode">
        <!-- <el-input placeholder="请选择会员卡权益" v-model="form.memberCode"></el-input> -->
        <el-select
          v-model="form.memberCode"
          placeholder="请选择会员卡"
          style="width: 100%"
        >
          <el-option
            v-for="item in memberList"
            :key="item.id"
            :label="item.cardName"
            :value="item.cardCode"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { depositMember } from "@/api/userInfoList";
import { memberList } from "@/api/viplList";
export default {
  props: ["getAdvertiseList"],
  data() {
    return {
      dialogVisible: false,
      memberList: [],
      form: {
        userId: "",
        memberCode: "",
      },
      rules: {
        memberCode: [{ required: true, message: "会员权益不能为空" }],
      },
    };
  },

  methods: {
    // 获取会员卡权益
    async memberListFn() {
      try {
        let res = await memberList();
        if (res.code === 200) {
          this.memberList = res.result || [];
        }
      } catch (error) {
        console.log(error);
      }
    },

    getPage(data) {
      this.dialogVisible = true;
      this.form = {
        userId: data.id,
        memberCode: "",
      };

      this.memberListFn();
    },
    handleClose() {
      this.dialogVisible = false;
    },

    /**
     * @description: 会员卡充值
     * @return {*}
     * @author: jinglin.gao
     */
    async depositMemberFn() {
      try {
        let res = await depositMember(this.form);
        if (res.code === 200) {
          this.$message({
            type: "success",
            message: "充值成功",
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
          this.depositMemberFn();
        } else {
          return false;
        }
      });
    },
  },
};
</script>
