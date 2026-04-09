import React from 'react'
import '../styles/Header.css'
import { Link, useLocation, useNavigate } from 'react-router-dom'

const pageTitles = {
    '/header': 'ヘッダ', /*記載例*/
    '/mypage': 'マイページ',
    '/stocks': '銘柄一覧',
    '/admin': '管理者',
    '/stocks/': '銘柄詳細',
}

function getTitle(pathname) {
    if (pageTitles[pathname]) return pageTitles[pathname];
    if (pathname.startsWith('/stocks/')) return '株価詳細';
    return '画面名';
}
function Header() {
    const location = useLocation();
    const navigate = useNavigate();
    const title = getTitle(location.pathname);
    const userName = localStorage.getItem('userName') || 'ゲスト';

    const handleLogout = () => {
        localStorage.removeItem('userName')
        navigate('/');
    };
  return (
    <header className='header-top'>
        <div className='header-title'>{title}</div>
        <div className='header-link'>
            {/* TODO: マイページのリンク要確認 */}
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