import { registerApi, fetchJson } from "../API/LoginRegisterApi";
import "../App.css";
import "../styles/LoginRegister.css";
import { useRef, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { getErrorMessage } from "../utils/errorUtil";
import UserNameField from "../components/UserNameField";
import EmailField from "../components/EmailField";
import PasswordField from "../components/PasswordField";


function RegisterPage() {
  const navigate = useNavigate();

  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [commonError, setCommonError] = useState("");
  
  const nameRef = useRef();
  const emailRef = useRef();
  const passwordRef = useRef();

  const handleRegister = async (e) => {
    e.preventDefault();
    setCommonError("");

    let valid = true;
    if (!nameRef.current.validate()) valid = false;
    if (!emailRef.current.validate()) valid = false;
    if (!passwordRef.current.validate()) valid = false;
    if (!valid) return;


    try {
      await fetchJson("http://localhost:8080/api/users/register", {
        method: "POST",
        body: JSON.stringify({
          name: userName,
          email,
          password,
        }),
      });

      alert("ユーザ登録成功");
      navigate("/");
    } catch (error) {
      if (error?.messageId === "E005") {
        emailRef.current.setError(error.message);
      } else if (error?.message) {
        setCommonError(error.message);
      } else {
        setCommonError("サーバーに接続できません。");
      }
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1 className="auth-title">ユーザ登録</h1>

        <p className="auth-subtitle">
          名前、メールアドレス、パスワードを入力してください
        </p >

        <p className="password-rule">
          ※パスワードは8文字以上16文字以下で入力してください。
          <br />
          英字・数字・記号のうち2種類以上を含める必要があります。
        </p >

        <form onSubmit={handleRegister}>
          <UserNameField
            ref={nameRef}
            value={userName}
            onChange={setUserName}
            placeholder="ユーザ名を入力"
          />

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

          {commonError && <p className="error-text">{commonError}</p >}

          <button type="submit" className="main-button">
            登録
          </button>
        </form>

        <div className="link-area">
          <Link to="/" className="sub-link">
            ログイン画面はこちら
          </Link>
        </div>
      </div>
    </div>
  );
}

export default RegisterPage;