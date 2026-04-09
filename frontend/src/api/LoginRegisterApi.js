// TODO: 担当者が実装予定
export const loginApi = async (email, password) => {
  return fetchJson("http://localhost:8080/api/users/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
};

export const registerApi = async (userData) => {
  return fetchJson("http://localhost:8080/api/users/register", {
    method: "POST",
    body: JSON.stringify(userData),
  });
};

// export const fetchJson = async (url, options = {}) => {
//   const response = await fetch(url, {
//     headers: { "Content-Type": "application/json" },
//     ...options,
//   });

//   const data = await response.json();

//   if (!response.ok) {
//     throw data; 
//   }

//   return data;
// };

//TODO: テスト用なので後で消す
export const fetchJson = async (url, options = {}) => {
  console.log("送信先:", url);
  console.log("送信データ:", options.body);
  // 一時的にダミーを返す
  return { success: true };
};