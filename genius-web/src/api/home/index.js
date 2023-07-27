// api/prompt/promptsTemplate

import request from "@/request/index.js";

// 获取提问信息
export function getPromptsTemplate() {
    return request({
        url: "/api/prompt/promptsTemplate",
        method: "get",
    });
}