import React from 'react'
import '../styls/Header.css'
import { useLocation } from 'react-router-dom'

const pageTitles = {
    '/header': 'ヘッダ', //記載例
    //TODO ログイン画面、マイページ画面以外を追加する。
}
function Header() {
    const location = useLocation();
    const title = pageTitles[location.pathname] || '画面名';
    const userName = localStorage.getItem('userName') || 'ゲスト';

    const handleLogout = () => {
        localStorage.removeItem('userName')
        window.location.href = '/'
    };
  return (
    <header className='header-top'>
        <div className='header-title'>{title}</div>
        <div className='header-link'>
            <div>{userName}</div>
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