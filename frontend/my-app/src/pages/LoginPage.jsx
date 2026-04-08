import React, { useRef, useState } from 'react'
import { getErrorMessage } from '../utils/errorUtil';
import { useNavigate, Link } from "react-router-dom";
import "../styls/LoginRegister.css";
import { loginApi } from "../API/LoginRegisterApi";
import EmailField from '../components/EmailField';
import PasswordField from '../components/PasswordField';



function LoginPage() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  
  const [loginError, setLoginError] = useState("");
  const [connectError, setConnectError] = useState("");

  const emailRef = useRef();
  const passwordRef = useRef();


  const handleLogin = async (e) => {
    e.preventDefault();

    setLoginError("");
    setConnectError("");

    let valid = true;
    if (!emailRef.current.validate()) valid = false;
    if (!passwordRef.current.validate()) valid = false;
    if (!valid) return;

    try {
      const response = await loginApi(email, password);
      const user = response.data;

      localStorage.setItem("loginUserId", user.id);
      localStorage.setItem("loginUserName", user.name);
      localStorage.setItem("loginUserEmail", user.email);
      localStorage.setItem("loginUserRole", user.role);
      
      //TODO: 正確な遷移先のパスを入れる
      if (user.role === "管理者") {
        navigate("/admin");
      } else {
        navigate("/users");
      }
    } catch (error) {
      console.error(error);
      if (error?.messageId) {
        setLoginError(error.message);
      } else {
        setConnectError(getErrorMessage("E007", "サーバー"));
      }
    }
  };

  return (
  <div className="auth-container">
      <div className="auth-box">
        <h1 className="auth-title">ログイン</h1>

        {loginError && (
          <p id="login_error_message" className="error-text">
            {loginError}
          </p >
        )}

        {connectError && (
          <p id="connect_error_message" className="error-text">
            {connectError}
          </p >
        )}

        <form onSubmit={handleLogin}>
          <EmailField
              ref={emailRef}
              value={email}
              onChange={setEmail}
              placeholder="メールアドレスを入力"
          />

          <PasswordField
              ref={passwordRef}
              value={password}
              onChange={setPassword}
              placeholder="パスワードを入力"
          />

          <button id="login_button" type="submit" className="main-button">
            ログイン
          </button>
        </form>

        <div className="link-area">
          <Link id="to_register_link" to="/register" className="sub-link">
            新規会員登録はこちら
          </Link>
        </div>
      </div>
    </div>
  );
}



export default LoginPage