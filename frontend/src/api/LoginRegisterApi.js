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

export const fetchJson = async (url, options = {}) => {
  const response = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  const data = await response.json();

  if (!response.ok) {
    throw data;
  }

  return data;
};