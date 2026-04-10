export function getLoginUserId() {
  return localStorage.getItem("loginUserId") || "";
}

export function getLoginUserName() {
  return localStorage.getItem("loginUserName") || "ゲスト";
}

export function getAuthHeaders(extraHeaders = {}) {
  return {
    "Content-Type": "application/json",
    "X-USER-ID": getLoginUserId(),
    ...extraHeaders,
  };
}