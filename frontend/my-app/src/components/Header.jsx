import React from 'react'
import '../styles/Header.css'
import { Link, useLocation, useNavigate } from 'react-router-dom'

const pageTitles = {
    '/header': 'ヘッダ', //記載例
    //TODO: ログイン画面、マイページ画面以外を追加する。
}
//TODO: 銘柄詳細のURLを確認
function getTitle(pathname) {
    if (pageTitles[pathname]) return pageTitles[pathname];
    if (pathname,startsWith('/stocks/')) return '株価詳細';
    return '画面名';
}
function Header() {
    const location = useLocation();
    const navigate = useNavigate();
    const title = getTitle(location.pathname);
    const userName = localStorage.getItem('userName') || 'ゲスト';

    const handleLogout = () => {
        localStorage.removeItem('userName')
        Navigate('/');
    };
  return (
    <header className='header-top'>
        <div className='header-title'>{title}</div>
        <div className='header-link'>
            //TODO: マイページのリンク要確認
            <Link to='/mypage'>{userName}</Link>
            <button 
                className='logout-btn'
                onClick={handleLogout}
            >
                ログアウト
            </button>
        </div>
    </header>
  )
}

export default Header