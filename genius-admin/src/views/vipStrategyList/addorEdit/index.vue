<template>
  <el-dialog
    v-if="dialogVisible"
    :destroy-on-close="true"
    :title="rowData ? '编辑权益' : '新增权益'"
    :visible.sync="dialogVisible"
    width="550px"
  >
    <el-form ref="form" :rules="rules" :model="vipStrategyListForm" label-width="120px">
      <el-form-item label="策略名称" prop="rightsName">
        <el-input
          placeholder="请输入策略名称"
          v-model="vipStrategyListForm.rightsName"
        ></el-input>
      </el-form-item>
      <el-form-item label="会员卡Code" prop="memberCode">
        <el-select
          style="width: 100%"
          v-model="vipStrategyListForm.memberCode"
          placeholder="请选择会员卡Code"
        >
          <el-option
            v-for="item in resMemberList"
            :key="item.id"
            :label="item.cardName"
            :value="item.cardCode"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="会员卡次数" prop="count">
        <el-input
          type="number"
          placeholder="请输入会员卡次数"
          v-model="vipStrategyListForm.count"
        ></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { memberRightsConfigSaveOrUpdate } from "@/api/vipStrategyList";
import { memberList } from "@/api/viplList";
export default {
  props: ["getMemberRightsList"],
  data() {
    return {
      rowData: null,
      dialogVisible: false,
      resMemberList: [],
      vipStrategyListForm: {
        memberCode: "",
        count: "",
        rightsName: "",
      },
      rules: {
        name: [{ required: true, message: "请输入名称" }],
        memberCode: [{ required: true, message: "请输入会员卡Code" }],
        count: [{ required: true, message: "请输入会员卡次数" }],
        rightsName: [{ required: true, message: "请输入策略名称" }],
      },
    };
  },
  methods: {
    getPage(data) {
      console.log(data, "2222");
      this.dialogVisible = true;
      this.vipStrategyListForm = {
        memberCode: "",
        count: "",
        rightsName: "",
      };

      this.getMemberList();
      if (data) {
        this.rowData = data;
        this.vipStrategyListForm = JSON.parse(JSON.stringify(data));
      }
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
          this.resMemberList = res.result || [];
        }
      } catch (error) {
        console.log(error);
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
    async memberRightsConfigSaveOrUpdateFn() {
      try {
        let res = await memberRightsConfigSaveOrUpdate(this.vipStrategyListForm);
        if (res.code === 200) {
          this.$message({
            type: "success",
            message: "新建权益成功",
          });
          this.handleClose(); // 关闭窗口 或 保存配置项 按钮被点击后 执行此函数
          this.getMemberRightsList();
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
          this.memberRightsConfigSaveOrUpdateFn();
        } else {
          return false;
        }
      });
    },
  },
};
</script>
