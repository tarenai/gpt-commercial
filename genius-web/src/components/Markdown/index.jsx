/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-03-29 17:32:18
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-18 13:13:43
 */
import React, { useRef } from "react";
import ReactMarkdown from "react-markdown";
import "katex/dist/katex.min.css";

import RemarkBreaks from "remark-breaks";
import RehypeRaw from "rehype-raw"; // 解析标签，支持html语法
import RehypeKatex from "rehype-katex"; //数学公式
import RemarkMath from "remark-math"; //数学公式
import RemarkGfm from "remark-gfm"; //配置好后即可使用表格、删除线、任务列表、引用等操作
import RehypeHighlight from "rehype-highlight";
import mermaid from "mermaid";
import { copyToClipboardFn } from "@/utils";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { okaidia } from "react-syntax-highlighter/dist/esm/styles/prism";
// import 'github-markdown-css';
let reactMarkTheme = okaidia;

export function PreCode(props) {
  const ref = useRef(null);

  return (
    <pre ref={ref}>
      <span
        className="copy-code-button"
        onClick={() => {
          if (ref.current) {
            const code = ref.current.innerText;
            copyToClipboardFn(code, "复制成功");
          }
        }}
      ></span>
      {props.children}
    </pre>
  );
}

function _MarkDownContent(props) {
  let customContent = props.content;

  return (
    <ReactMarkdown
      remarkPlugins={[RemarkMath, RemarkGfm, RemarkBreaks]}
      rehypePlugins={[RehypeRaw]}
      components={{
        pre: PreCode,
        code({ node, inline, className, children, ...props }) {
          const match = /language-(\w+)/.exec(className || "");
          let getLanguage = "";
          if (match) {
            let targetLg = match[1];
            getLanguage = targetLg.split("import")[0];
            console.log(getLanguage, "getLanguage");
          }
          return !inline && match ? (
            <SyntaxHighlighter
              {...props}
              children={String(children).replace(/\n$/, "")}
              style={reactMarkTheme}
              language={getLanguage || ""}
              PreTag="div"
            />
          ) : (
            <code {...props} className={className}>
              {children}
            </code>
          );
        },
      }}
    >
      {customContent}
    </ReactMarkdown>
  );
}

export const MarkdownContent = React.memo(_MarkDownContent);

export function Markdown(props) {
  console.log(props, "MarkdownProps");
  const mdRef = useRef(null);

  return (
    <div
      className="markdown-body"
      style={{
        fontSize: `${props.fontSize ?? 14}px`,
        height: "auto",
      }}
      ref={mdRef}
      onContextMenu={props.onContextMenu}
      onDoubleClickCapture={props.onDoubleClickCapture}
    >
      <MarkdownContent content={props.content} />
    </div>
  );
}
