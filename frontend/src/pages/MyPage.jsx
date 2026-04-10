import { useEffect, useRef, useState } from "react";
import StockListTable from "../components/StockListTable";
import Pagination from "../components/Pagination";
import {
  fetchMyInfo,
  updateMyInfo,
  fetchMyFavorites,
  removeFavorite,
} from "../api/userApi";
import "../styles/MyPage.css";
import UserNameField from "../components/UserNameField";
import EmailField from "../components/EmailField";
import Header from "../components/Header";

const PAGE_SIZE = 20;

function normalizeUserName(value) {
  return value.replace(/[\s　]+/g, "");
}

function validateUserName(value) {
  if (!value) return "ユーザ名は必須です。";
  if (value.length > 30) return "ユーザ名は30文字までです。";
  if (/[ -~]/.test(value)) return "正しいユーザ名を入力してください。";
  return "";
}

function validateEmail(value) {
  if (!value) return "メールアドレスは必須です。";
  if (value.length > 50) return "メールアドレスは50文字までです。";
  const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
  if (!regex.test(value)) return "正しいメールアドレスを入力してください。";
  return "";
}

function MyPage() {
  const [mode, setMode] = useState("display");
  const [message, setMessage] = useState("");
  const [currentPage, setCurrentPage] = useState(0);

  const [userInfo, setUserInfo] = useState({ userName: "", email: "" });
  const [form, setForm] = useState({ userName: "", email: "" });
  const [errors, setErrors] = useState({ userName: "", email: "" });
  const [favoriteData, setFavoriteData] = useState({
    totalFavorites: 0,
    page: 0,
    size: 20,
    totalPages: 0,
    currentFavoriteCount: 0,
    maxFavoriteCount: 20,
    items: [],
  });

  useEffect(() => {
    initialize();
  }, []);

  useEffect(() => {
    loadFavorites(currentPage);
  }, [currentPage]);

  async function initialize() {
    try {
      const userRes = await fetchMyInfo();
      const user = userRes.data;

      setUserInfo({
        userName: user.userName || "",
        email: user.email || "",
      });
      setForm({
        userName: user.userName || "",
        email: user.email || "",
      });

      const favoriteRes = await fetchMyFavorites(0, PAGE_SIZE);
      setFavoriteData(favoriteRes.data);
    } catch (error) {
      setMessage(error.message);
    }
  }

  async function loadFavorites(page) {
    try {
      const res = await fetchMyFavorites(page, PAGE_SIZE);
      setFavoriteData(res.data);
    } catch (error) {
      setMessage(error.message);
    }
  }

  const userNameRef = useRef();
  const emailRef = useRef();

  async function handleUpdate() {
    const isUserNameValid = userNameRef.current.validate();
    const isEmailValid = emailRef.current.validate();
    if (!isUserNameValid || !isEmailValid) return;

    const normalizedUserName = normalizeUserName(form.userName);
    const trimmedEmail = form.email.trim();

    const nextErrors = {
      userName: validateUserName(normalizedUserName),
      email: validateEmail(trimmedEmail),
    };
    setErrors(nextErrors);

    if (nextErrors.userName || nextErrors.email) return;

    try {
      const res = await updateMyInfo({
        userName: normalizedUserName,
        email: trimmedEmail,
      });

      setUserInfo({
        userName: res.data.userName,
        email: res.data.email,
      });
      setForm({
        userName: res.data.userName,
        email: res.data.email,
      });
      setMode("display");
      setMessage(res.message || "更新しました。");
    } catch (error) {
      setMessage(error.message);
    }
  }

  async function handleRemoveFavorite(tickerCode) {
    try {
      const res = await removeFavorite(tickerCode);
      setMessage(res.message || "お気に入り解除しました。");
      await loadFavorites(currentPage);
    } catch (error) {
      setMessage(error.message);
    }
  }

  return (
    <div className="mypage-screen">
      <Header />

      <div className="mypage-page">
        {message && <div className="page-message">{message}</div>}

        <div className="mypage-user-box">
          {mode === "display" ? (
            <>
              <div className="mypage-display-row">
                <div className="mypage-display-label">ユーザ名</div>
                <div className="mypage-display-value">{userInfo.userName}</div>
              </div>
              <div className="mypage-display-row">
                <div className="mypage-display-label">メールアドレス</div>
                <div className="mypage-display-value">{userInfo.email}</div>
              </div>
              <div className="mypage-button-area">
                <button
                  type="button"
                  className="mypage-button mypage-button-edit"
                  onClick={() => {
                    setMode("input");
                    setErrors({ userName: "", email: "" });
                    setMessage("");
                  }}
                >
                  編集
                </button>
              </div>
            </>
          ) : (
            <>
              <UserNameField
                ref={userNameRef}
                value={form.userName}
                onChange={(value) => setForm((prev) => ({ ...prev, userName: value }))}
                placeholder="ユーザ名"
              />
              <EmailField
                ref={emailRef}
                value={form.email}
                onChange={(value) => setForm((prev) => ({ ...prev, email: value }))}
                placeholder="メールアドレス"
              />
              <div className="mypage-button-area">
                <button
                  type="button"
                  className="mypage-button mypage-button-update"
                  onClick={handleUpdate}
                >
                  更新
                </button>
              </div>
            </>
          )}
        </div>

        <StockListTable
          title="お気に入り銘柄"
          currentCount={favoriteData.currentFavoriteCount}
          maxCount={favoriteData.maxFavoriteCount}
          items={favoriteData.items}
          onToggleFavorite={handleRemoveFavorite}
          fromPath="/mypage"
        />

        <Pagination
          currentPage={currentPage}
          totalCount={favoriteData.totalFavorites}
          pageSize={PAGE_SIZE}
          onPageChange={setCurrentPage}
        />
      </div>
    </div>
  );
}

export default MyPage;