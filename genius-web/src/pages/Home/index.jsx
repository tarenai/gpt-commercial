/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-10-12 13:45:29
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-19 08:56:25
 */
import React, { useState, useRef, useEffect } from "react";
import moment from "moment";
import styles from "./index.module.less";
import BotIcon from "@/assets/imgs/icons/bot.svg";
import Send from "@/assets/imgs/icons/send.svg";
import { Markdown } from "@/components/Markdown";
import advbg from "../../../public/assets/imgs/adv-bg.png";
import {
  sessionRecordSidebar,
  createSession,
  getSessionDetail,
  removeSession,
  getAdvertiseList,
} from "@/api/gpt";
import { Button, Tabs, Modal, Carousel } from "antd";
import {
  PlusCircleOutlined,
  CloseOutlined,
  ExclamationCircleOutlined,
} from "@ant-design/icons";
import { getCookie, copyToClipboardFn, messageFn } from "@/utils";
import _ from "lodash";
import Loading from "@/components/Loading";
import { useSelector } from "react-redux";
import PromptsTemplate from "./components/PromptsTemplate";
import userDeafultImg from "../../../public/assets/imgs/userDeafultImg.svg";
import { useHistory } from "react-router-dom";
const { confirm } = Modal;
const Home = () => {
  const history = useHistory();
  // 消息模板
  let messageObj = {
    reqId: "",
    content: "",
    chatRole: "",
    createTime: "",
  };
  const userInfo = useSelector((state) => state.userInfo);
  // 会话列表
  const [sessionList, setSessionList] = useState([]);
  const sessionListRef = useRef(null);
  // 当前会话
  const [activeSession, setActiveSessions] = useState(null);

  // 消息列表
  const [messageList, setMessageList] = useState([]);
  const messageRefList = useRef([]);

  // 消息回答状态
  const [messageState, setMessageState] = useState(false);

  // 提问内容
  const [userQuestion, setUserQuestion] = useState("");

  // 当前提问下标
  const activeChatIndex = useRef(0);

  // 问题回答提示框ref
  const chatBodyRef = useRef(null);

  // 长连接EventSource ref
  const eventSourceRef = useRef(null);

  // 移动端兼容适配
  // 0代表ai 赋能 1 代表 提问
  const [tabBarType, setTabBarType] = useState("0");

  // 回答选择和会话详情显示
  // 0代表选择会话 1代表查看详情
  const [sessionType, setSessionType] = useState("0");

  // 广告列表
  const [advList, setAdvList] = useState([]);
  /**
   * @description: 回车提问
   * @param {*} e
   * @return {*}
   * @author: jinglin.gao
   */
  document.onkeydown = function (e) {
    // 回车提交表单
    // 兼容FF和IE和Opera
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code === 13 && e.altKey) {
      e.preventDefault();
      let anwserText = userQuestion + "\n";
      setUserQuestion(anwserText);
    } else if (e.keyCode === 13) {
      e.preventDefault(); //禁止回车的默认换行
      userAnswer();
    }
  };

  useEffect(() => {
    getSessionRecordSidebar();
    getAdvList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * @description:
   * @return {*}
   * @author: jinglin.gao
   */
  // getAdvertiseList
  const getAdvList = async () => {
    try {
      let res = await getAdvertiseList();
      if (res.code === 200) {
        setAdvList(res.result || []);
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 设置会话滚动条处于页面底部
   * @return {*}
   * @author: jinglin.gao
   */
  const mackSessionScrollToBottom = () => {
    let ele = chatBodyRef.current;

    setTimeout(() => {
      if (ele) {
        if (ele.scrollHeight > ele.clientHeight) {
          ele.scrollTop = ele.scrollHeight + 50;
        }
      }
    });
  };

  /**
   * @description: 提问异常处理
   * @return {*}
   * @author: jinglin.gao
   */
  const rganizeResultsByCode = ({ code, message }) => {
    if (!code) return true;

    switch (code) {
      // 登录失败~
      case 4001:
        confirm({
          title: "尊敬的用户您好!",
          icon: <ExclamationCircleOutlined />,
          content: "当前您暂未登录,或登录状态已经失效,点击按钮,跳转登录页",
          onOk() {
            history.replace("/login");
          },
        });
        break;
      // 请登录后再进行使用
      case 4002:
        confirm({
          title: "尊敬的用户您好!",
          icon: <ExclamationCircleOutlined />,
          content: "当前您暂未登录,或登录状态已经失效,点击按钮,跳转登录页",
          onOk() {
            history.replace("/login");
          },
        });
        break;
      //
      case 4003:
        confirm({
          title: "尊敬的用户您好!",
          icon: <ExclamationCircleOutlined />,
          content: "您的余额不足请充值后再使用,点击按钮,前往充值",
          onOk() {
            history.replace("/ai/commodity");
          },
        });
        break;
      // 您的会员已过期，请充值
      case 4004:
        confirm({
          title: "尊敬的用户您好!",
          icon: <ExclamationCircleOutlined />,
          content: "您的会员已过期,请充值,点击按钮,前往充值",
          onOk() {
            history.replace("/ai/commodity");
          },
        });

        break;
      // 您的提问次数不足，请充值
      case 4005:
        confirm({
          title: "尊敬的用户您好!",
          icon: <ExclamationCircleOutlined />,
          content: "您的提问次数不足，请充值,点击按钮,前往充值",
          onOk() {
            history.replace("/ai/commodity");
          },
        });
        break;
      default:
        break;
    }

    setMessageState(false);
    return false;
  };

  /**
   * @description: 提问
   * @return {*}
   * @author: jinglin.gao
   */
  const userAnswer = () => {
    if (messageState) {
      // messageF
      messageFn({
        type: "info",
        content: "Genius 正在回答您的问题,请稍后在提问,或停止Genius的回答",
      });
      return;
    }

    if (!userQuestion) return;
    // 设置滚动条在页面最底部
    mackSessionScrollToBottom();
    console.log("执行几次");
    // 用户提问
    let userQuestionData = _.cloneDeep(messageObj);
    userQuestionData.content = userQuestion;
    userQuestionData.reqId = new Date().getTime() + "_user";
    userQuestionData.createTime = moment(new Date()).format(
      "YYYY-MM-DD HH:mm:ss"
    );
    userQuestionData.chatRole = "user";
    messageRefList.current.push(userQuestionData);
    // ai 回答
    let assistantAnswer = _.cloneDeep(messageObj);
    assistantAnswer.reqId = new Date().getTime() + "_assistantAnswer";
    assistantAnswer.createTime = moment(new Date()).format(
      "YYYY-MM-DD HH:mm:ss"
    );
    assistantAnswer.chatRole = "assistant";
    messageRefList.current.push(assistantAnswer);
    setMessageList([...messageRefList.current]);

    // 找到当前需要回答问题的数据 更新数据
    activeChatIndex.current = messageRefList.current.length - 1;

    let str = "";
    let tokenName = getCookie("tokenName");
    let tokenValue = getCookie("tokenValue");

    // 清空提问框
    setUserQuestion("");
    let questionUrl = window.location.origin;
    eventSourceRef.current = new EventSource(
      `${questionUrl}/api/chat/questions?prompt=${encodeURIComponent(
        userQuestion
      )}&reqId=${activeSession.reqId}&${tokenName}=Bearer ${tokenValue}`
    );

    eventSourceRef.current.onmessage = function (event) {
      // 设置滚动条在页面最底部
      mackSessionScrollToBottom();
      setMessageState(true);
      if (event.data === "DONE") {
        setMessageState(false);
        closeAnswer();
        getSessionRecordSidebar();
      } else {
        let resData = JSON.parse(event.data);

        // 提问异常处理
        if (!rganizeResultsByCode(resData)) return;

        str = str + resData?.content || "";
        let activeChat = messageRefList.current[activeChatIndex.current];

        console.log(activeChat, "activeChatactiveChat");

        if (activeChat) {
          activeChat.content = str ? str : "";
          messageRefList.current.splice(activeChatIndex.current, 1, activeChat);

          console.log(messageRefList.current, "messageRefList.current");

          setMessageList([...messageRefList.current]);
        }
      }
    };

    eventSourceRef.current.onerror = function (event) {
      // 处理错误
      closeAnswer();
    };
  };

  /**
   * @description: 停止回答问题
   * @return {*}
   * @author: jinglin.gao
   */

  const closeAnswer = (handlerType) => {
    if (eventSourceRef.current) {
      eventSourceRef.current.close();

      if (handlerType) {
        messageFn({
          type: "success",
          content: "Genius 已停止当前会话的回复",
        });
        setMessageState(false);
      }
    }
  };

  /**
   * @description: 修改提问你
   * @return {*}
   * @author: jinglin.gao
   */

  const textareaChannge = (e) => {
    setUserQuestion(e.target.value);
  };

  /**
   * @description: 获取会话列表
   * @return {*}
   * @author: jinglin.gao
   */
  const getSessionRecordSidebar = async () => {
    try {
      let res = await sessionRecordSidebar();
      console.log(res, "2222");
      if (res.code === 200) {
        // 第一次刷新左侧列表要默认展示一条会话详细内容
        if (res.result.length) {
          setSessionList(res.result || []);
          sessionListRef.current = res.result || [];
          if (!activeSession) {
            setActiveSessions(res.result[0]);
            getSessionDetailFn(res.result[0]);
          }
        } else {
          console.log(sessionList, "sessionListsessionList");
          setSessionList([]);
          sessionListRef.current = []; // 清空列表展示选择展示选项选择的选项卡选项卡选择选项卡
          // 如果查询会话列表为空则需要清除右侧对话
          setMessageList([]);

          messageRefList.current = [];
        }
      }
    } catch (error) {
      console.log(error);
    }
  };
  useEffect(() => {
    if (sessionList.length === 0 && sessionListRef.current) {
      createSessionFn();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sessionList]);

  /**
   * @description: 新建会话
   * @return {*}
   * @author: jinglin.gao
   */
  const createSessionFn = async () => {
    try {
      let res = await createSession();
      if (res.code === 200) {
        const { result } = res;
        let sessionItem = {
          reqId: result,
          content: "新的会话",
        };
        closeAnswer();
        messageRefList.current = [];
        setMessageList([]);
        setActiveSessions(sessionItem);
        console.log(sessionList, "sessionListsessionList222222");
        let sessionListCopyData = _.cloneDeep(sessionList);
        setSessionList([sessionItem, ...sessionListCopyData]);
      }
      console.log(res);
    } catch (error) {
      console.log(error);
    }
  };
  /**
   * @description: 选择会话查询
   * @return {*}
   * @author: jinglin.gao
   */

  const chooseSession = (v) => {
    setActiveSessions(v);
    getSessionDetailFn(v);
  };

  /**
   * @description: 查询当前会话的具体内容
   * @return {*}
   * @author: jinglin.gao
   */
  const getSessionDetailFn = async ({ reqId }) => {
    try {
      let params = {
        reqId,
      };
      let res = await getSessionDetail(params);
      if (res.code === 200) {
        setMessageList(res.result || []);
        messageRefList.current = res.result || [];
        mackSessionScrollToBottom();
        setSessionType("1");
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 删除会话
   * @return {*}
   * @author: jinglin.gao
   */
  const deleteSessionFn = async (e, { reqId }) => {
    e.stopPropagation();
    try {
      let params = {
        reqId,
      };
      let res = await removeSession(params);
      if (res.code === 200) {
        messageFn({
          type: "success",
          content: "会话删除成功",
        });

        let deletedSessionList = sessionList.filter(
          (v) => v.reqId !== reqId && v.chatRole
        );

        console.log(deletedSessionList, "deletedSessionList");
        if (deletedSessionList.length) {
          setActiveSessions(deletedSessionList[0]);
          getSessionDetailFn(deletedSessionList[0]);
        } else {
          setActiveSessions(null);
          setSessionList([]);
        }
        getSessionRecordSidebar();
      } else {
        messageFn({
          type: "error",
          content: res.message,
        });
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 复制会话内容
   * @return {*}
   * @author: jinglin.gao
   */
  const copyToClipboard = (e, data) => {
    e.stopPropagation();
    copyToClipboardFn(data, "复制内容成功");
  };

  // 打开广告
  const openAdv = (data) => {
    window.open(data.advertiseLink);
  };

  return (
    <div className={styles.home_container_warp}>
      <div className="mobile_tools-bar">
        <Tabs
          onChange={(e) => {
            console.log(e, "111111");
            setTabBarType(e);
          }}
          value={tabBarType}
          centered
        >
          <Tabs.TabPane tab="Ai 赋能" key="0"></Tabs.TabPane>
          <Tabs.TabPane tab="提问" key="1"></Tabs.TabPane>
        </Tabs>
      </div>

      <div className="home_container_pc">
        {/* 赋能列表 */}
        <div
          className={`home_container-menu ${
            tabBarType === "0" ? "menuShow" : ""
          }`}
        >
          <PromptsTemplate />
        </div>

        {/* 对话区域 */}

        <div
          className="home_container-warp"
          style={{ width: advList.length ? "calc(80% - 180px)" : "80%" }}
        >
          <div className="home_container">
            <div
              className={`home_sidebar ${
                sessionType === "0" ? "sessionShow" : ""
              }`}
            >
              <div className="home_sidebar-header">
                <div className="home_sidebar-title">选择会话</div>
              </div>
              <div className="home_sidebar-body">
                {sessionList.map((v) => (
                  <div
                    onClick={() => chooseSession(v)}
                    key={v.reqId}
                    className={`home_chat-item ${
                      activeSession?.reqId === v.reqId
                        ? "home_chat-item-selected"
                        : ""
                    }`}
                  >
                    <div className="home_chat-item-title">{v.content}</div>

                    <div className="home_chat-item-info">
                      <div>{v.reqCount ? v.reqCount : 0} 条对话</div>
                      <div>
                        {v.createTime
                          ? v.createTime
                          : moment(new Date()).format("YYYY-MM-DD HH:mm:ss")}
                      </div>
                    </div>

                    {/* 最后一条会话禁止删除 */}

                    {sessionList.length > 1 ? (
                      <div className="home_chat-item-delete">
                        <CloseOutlined
                          onClick={(e) => deleteSessionFn(e, v)}
                        ></CloseOutlined>
                      </div>
                    ) : (
                      ""
                    )}
                  </div>
                ))}
              </div>
              <div className="home_sidebar-tail">
                <Button
                  onClick={_.debounce(createSessionFn, 500)}
                  icon={<PlusCircleOutlined />}
                  type="primary"
                >
                  新的会话
                </Button>
              </div>
            </div>
            <div
              className={`home_window-content ${
                sessionType === "1" ? "sessionShow" : ""
              }`}
            >
              <div className="home_chat">
                <div className="home_window-header">
                  <div className="home_window-header-title">
                    <div className="home_window-header-main-title">
                      {activeSession?.content}
                    </div>

                    <Button
                      className="mobile_goback-sesion"
                      onClick={() => setSessionType("0")}
                    >
                      返回会话
                    </Button>

                    {/* <div className="home_window-header-sub-title">
                    共计 {activeSession.reqCount || 0} 条消息
                  </div> */}
                  </div>
                </div>
                <div className="home_chat-body" ref={chatBodyRef}>
                  {messageList.map((chatItem, index) => (
                    <div key={chatItem.reqId + "_" + index}>
                      {chatItem.chatRole === "assistant" ? (
                        <div className="home_chat-message__ai">
                          <div className="home_chat-message-container">
                            <div className="home_chat-message-avatar">
                              <img
                                className="home_user-avtar"
                                src={BotIcon}
                                alt=""
                              />
                            </div>

                            <div className="home_chat-message-item">
                              <div className="home_chat-message-top-actions">
                                <div
                                  onClick={(e) =>
                                    copyToClipboard(e, chatItem.content)
                                  }
                                  className="home_chat-message-top-action"
                                >
                                  复制
                                </div>
                              </div>

                              <div className="markdown-body">
                                {chatItem.content ? (
                                  <Markdown
                                    content={
                                      chatItem.content ? chatItem.content : ""
                                    }
                                  ></Markdown>
                                ) : (
                                  <Loading></Loading>
                                )}
                              </div>
                            </div>

                            <div className="home_chat-message-actions">
                              <div className="home_chat-message-action-date">
                                {chatItem.createTime}
                              </div>
                            </div>
                          </div>
                        </div>
                      ) : chatItem.chatRole === "user" ? (
                        <div className="home_chat-message-user">
                          <div className="home_chat-message-container">
                            <div className="home_chat-message-avatar">
                              <img
                                className="home_user-avtar"
                                src={userInfo?.headImgUrl || userDeafultImg}
                                alt=""
                              />
                            </div>

                            <div className="home_chat-message-item">
                              <div className="home_chat-user-message">
                                {chatItem.content}
                              </div>

                              <div className="home_chat-message-top-actions">
                                <div
                                  onClick={(e) =>
                                    copyToClipboard(e, chatItem.content)
                                  }
                                  className="home_chat-message-top-action"
                                >
                                  复制
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      ) : (
                        ""
                      )}
                    </div>
                  ))}
                </div>
                <div className="home_chat-input-panel">
                  <div
                    style={{ opacity: messageState ? 1 : 0 }}
                    className="home_chat-bottom-tools"
                  >
                    <Button onClick={() => closeAnswer(true)}>停止回答</Button>
                  </div>

                  <div className="home_chat-input-panel-inner">
                    <textarea
                      value={userQuestion}
                      onChange={textareaChannge}
                      className="home_chat-input"
                      placeholder="输入消息，单机发送按钮,或按Enter键发送。Alt+Enter文本换行"
                      rows="4"
                    ></textarea>

                    <div
                      className="button_icon-button home_chat-input-send no-dark"
                      onClick={userAnswer}
                    >
                      <div className="button_icon-button-icon">
                        <img src={Send} alt="" />
                      </div>
                      <div className="button_icon-button-text">发送</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* 广告位 */}
        {advList.length ? (
          <div className="home_container-adv">
            <Carousel className="" autoplay dotPosition="top">
              {advList.map((v) => {
                return (
                  <div key={v.id} className="adv-item">
                    <div className="adv-info">
                      <p className="adv-info-title">{v.advertiseName}</p>
                      <img
                        onClick={() => openAdv(v)}
                        className="advImg"
                        src={v.imgLink}
                        alt=""
                      />
                    </div>
                  </div>
                );
              })}
            </Carousel>
          </div>
        ) : (
          ""
        )}
      </div>
    </div>
  );
};

export default Home;
