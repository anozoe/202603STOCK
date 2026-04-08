import { fetchJson } from "../API/LoginRegisterApi";
import "../App.css";
import "../styls/LoginRegister.css";
import { useRef, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { getErrorMessage } from "../utils/errorUtil";
import UserNameField from "../components/UserNameField";
import EmailField from "../components/EmailField";
import PasswordField from "../components/PasswordField";


function RegisterPage() {
  const navigate = useNavigate();

  const [user_name, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [error_message, setErrorMessage] = useState("");
  
  const nameRef = useRef();
  const emailRef = useRef();
  const passwordRef = useRef();

  const handleRegister = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    let valid = true;
    if (!nameRef.current.validate()) valid = false;
    if (!emailRef.current.validate()) valid = false;
    if (!passwordRef.current.validate()) valid = false;
    if (!valid) return;


    try {
      await fetchJson("http://localhost:8080/api/users/register", {
        method: "POST",
        body: JSON.stringify({
          name: user_name,
          email,
          password,
        }),
      });

      navigate("/");
    } catch (error) {
      if (error?.messageId === "E005") {
        emailRef.current.setError(error.message);
      } else if (error?.message) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage(getErrorMessage("E013", "ユーザ登録"));
      }
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1 className="title_register">ユーザ登録</h1>

        <p className="register_guide">
          名前、メールアドレス、パスワードを入力してください。
          <p></p>
          ※パスワードは8文字以上16文字以下で入力してください。
          <p></p>
          英字・数字・記号のうち2種類以上を含める必要があります。
        </p >
        
        {error_message && <p className="error-text">{error_message}</p >}

        <form onSubmit={handleRegister}>
          <UserNameField
            ref={nameRef}
            value={user_name}
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


          <button type="submit" className="main-button">
            登録
          </button>
        </form>

        <div className="link-area">
          <button 
            className="sub-button"
            onClick={() => navigate("/")}
          >
            ログインへ
          </button>
        </div>
      </div>
    </div>
  );
}

export default RegisterPage;