/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-09 18:14:13
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-09 19:59:13
 */
import React, { useEffect, useState, useRef } from "react";
import styles from "./index.module.less";
import { getPromptsTemplate } from "@/api/home";
import { copyToClipboardFn } from "@/utils";
import { Input, Button } from "antd";
const PromptsTemplate = () => {
  const [templateList, setTemplateList] = useState([]);
  const templateListRef = useRef(null);
  const templateSearchRef = useRef("");
  useEffect(() => {
    getPromptsTemplateFn();
  }, []);
  /**
   * @description: 获取模板信息
   * @return {*}
   * @author: jinglin.gao
   */
  const getPromptsTemplateFn = async () => {
    try {
      let res = await getPromptsTemplate();
      if (res.code === 200) {
        templateListRef.current = res.result;
        setTemplateList(res.result);
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 复制粘贴
   * @return {*}
   * @author: jinglin.gao
   */
  const copyPrompt = (data) => {
    copyToClipboardFn(
      data,
      "复制成功,赶快粘贴到提问框,赋予Genius全新的人格吧。"
    );
  };

  /**
   * @description: 搜索框内容
   * @return {*}
   * @author: jinglin.gao
   */
  const templateSearch = (e) => {
    templateSearchRef.current = e.target.value;
    if (!templateSearchRef.current) {
      setTemplateList(templateListRef.current);
    }
  };

  /**
   * @description: 搜索数据过滤
   * @return {*}
   * @author: jinglin.gao
   */
  const searchTemplateFn = () => {
    if (!templateSearchRef.current) {
      setTemplateList(templateListRef.current);
    } else {
      let filterDataList = templateList.filter(
        (v) =>
          v.prompt.indexOf(templateSearchRef.current) !== -1 ||
          v.act.indexOf(templateSearchRef.current) !== -1
      );
      setTemplateList(filterDataList);
    }
  };
  return (
    <div className={styles.promptsTemplateWarp}>
      <div className="title_box">
        <p className="title">Ai人格赋能</p>
        {/* <Input onChange={templateSearch} placeholder="请输入您想使用的模板" /> */}
        <Input.Group compact>
          <Input
            style={{
              width: "calc(100% - 64px)",
            }}
            onChange={templateSearch}
            placeholder="请输入您想使用的模板"
          />
          <Button onClick={searchTemplateFn} type="primary">
            搜索
          </Button>
        </Input.Group>
      </div>

      <div className="prompts-template-box">
        {templateList.map((v) => (
          <div
            title={v.prompt}
            key={v.act + new Date().getTime()}
            className="prompts_template-box-item"
          >
            <p className="act">{v.act}</p>
            <p className="prompt">{v.prompt}</p>

            <p onClick={() => copyPrompt(v.prompt)} className="copy">
              复制
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default PromptsTemplate;
