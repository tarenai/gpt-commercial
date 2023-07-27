<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-17 09:41:38
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-18 08:45:05
-->
<template>
  <div class="chatgpt_config-warp">
    <!-- chatgpt配置 -->
    <div class="config-box">
      <el-form
        :model="chatForm"
        :rules="chatFormRules"
        ref="chatForm"
        label-width="200px"
      >
        <el-form-item label="apiKey列表" prop="apiKeys">
          <div class="apiKeyList">
            <div
              class="apiKey-item"
              v-for="(item, index) in chatForm.apiKeys"
              :key="index"
            >
              <el-input v-model="chatForm.apiKeys[index]"></el-input>
              <el-button
                :disabled="index === 0"
                @click="deleteKey(index)"
                size="small"
                type="danger"
                >删除</el-button
              >
            </div>
            <el-button @click="addKey" class="addKey" size="small" type="primary"
              >新增key</el-button
            >
          </div>
        </el-form-item>

        <el-form-item label="模型" prop="model">
          <el-select
            placeholder="请选择模型"
            style="width: 100%"
            v-model="chatForm.model"
          >
            <el-option
              v-for="item in modelList"
              :key="item.key"
              :label="item.key"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="temperature" prop="temperature">
          <el-input-number
            :min="0"
            :max="100"
            style="width: 100%"
            placeholder="请填写temperature"
            v-model="chatForm.temperature"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="maxTokens(最大支持2048)" prop="maxTokens">
          <el-input-number
            :min="0"
            :max="2048"
            style="width: 100%"
            placeholder="请填写maxTokens(最大支持2048)"
            v-model="chatForm.maxTokens"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="创造性和独特性设置(-2.0~2.0)" prop="presencePenalty">
          <el-input-number
            :min="-2"
            :max="2"
            style="width: 100%"
            placeholder="请填写创造性和独特性设置(-2.0~2.0)"
            v-model="chatForm.presencePenalty"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="多样化的文本设置(-2.0~2.0)" prop="frequencyPenalty">
          <el-input-number
            :min="-2"
            :max="2"
            style="width: 100%"
            placeholder="请填写多样化的文本设置(-2.0~2.0)"
            v-model="chatForm.frequencyPenalty"
          ></el-input-number>
        </el-form-item>

        <el-form-item style="text-align: center">
          <el-button type="primary" @click="onFormSubmit">保存配置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {
  chatModelList,
  saveOrUpdateChatConfig,
  getChatConfig,
} from "@/api/chatgptConfig";
export default {
  data() {
    return {
      modelList: [],
      chatForm: {
        apiKeys: [],
        model: "",
        temperature: 0,
        maxTokens: 0,
        presencePenalty: 0, // -2.0 to 2.0, 0 means no penalty.
        frequencyPenalty: 0,
      },
      chatFormRules: {
        apiKeys: [
          {
            required: true,
            message: "请输入apiKey",
          },
        ],
        model: [
          {
            required: true,
            message: "请选择chatgpt模型",
          },
        ],
        temperature: [
          {
            required: true,
            message: "请填写temperature",
          },
        ],

        maxTokens: [
          {
            required: true,
            message: "请填写maxTokens",
          },
        ],
        presencePenalty: [
          {
            required: true,
            message: "请填写创造性和独特性设置(-2.0~2.0)",
          },
        ],

        frequencyPenalty: [
          {
            required: true,
            message: "请填写多样化的文本设置(-2.0~2.0)",
          },
        ],
      },
    };
  },
  mounted() {
    this.chatModelListFn();
    this.getChatConfigFn();
  },
  methods: {
    // 获取gpt配置
    async getChatConfigFn() {
      try {
        let res = await getChatConfig();
        if (res.code === 200) {
          this.chatForm = res.result || {};
          if (!this.chatForm.apiKeys) {
            this.chatForm.apiKeys = [];
          }
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * @description: 获取模型列表
     * @return {*}
     * @author: jinglin.gao
     */
    async chatModelListFn() {
      try {
        let res = await chatModelList();
        if (res.code === 200) {
          this.modelList = res.result || [];
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * @description: 新增key
     * @return {*}
     * @author: jinglin.gao
     */
    addKey() {
      if (this.chatForm.apiKeys.some((v) => v === "")) {
        this.$message({
          type: "info",
          message: "当前存在尚未填写完毕的key,请填写完毕后再次新增",
        });
      } else {
        this.chatForm.apiKeys.push("");
      }
    },
    /**
     * @description: 删除key
     * @param {*} index
     * @return {*}
     * @author: jinglin.gao
     */
    deleteKey(index) {
      this.chatForm.apiKeys.splice(index, 1);
    },

    /**
     * @description: 保存配置
     * @return {*}
     * @author: jinglin.gao
     */
    async saveOrUpdateChatConfigFn() {
      try {
        let res = await saveOrUpdateChatConfig(this.chatForm);
        if (res.code === 200) {
          this.$message({
            type: "success",
            message: "配置保存成功",
          });
        } else {
          this.$message.error(res.msg);
        }
      } catch (error) {
        console.log(error);
      }
    },
    onFormSubmit() {
      this.$refs.chatForm.validate((valid) => {
        if (valid) {
          this.saveOrUpdateChatConfigFn();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
