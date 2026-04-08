import { useEffect, useState } from "react";
import Header from "../components/Header";
import Pagination from "../components/Pagination";
import {
  fetchAdminStocks,
  createAdminStock,
  updateAdminStock,
  deleteAdminStock,
  reorderAdminStocks,
  fetchAdminUsers,
  deleteAdminUser,
} from "../api/adminApi";
import {
  DndContext,
  PointerSensor,
  useSensor,
  useSensors,
  closestCenter,
} from "@dnd-kit/core";
import {
  SortableContext,
  useSortable,
  verticalListSortingStrategy,
  arrayMove,
} from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import "../styles/AdminPage.css";

const PAGE_SIZE = 20;

function SortableStockRow({ stock, rowNumber, onEdit, onDelete }) {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({ id: stock.id });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  return (
    <tr ref={setNodeRef} style={style} className={isDragging ? "admin-row-dragging" : ""}>
      <td className="admin-drag-cell">
        <button type="button" className="admin-drag-handle" {...attributes} {...listeners}>
          ☰
        </button>
      </td>
      <td>{rowNumber}</td>
      <td>{stock.tickerCode}</td>
      <td>{stock.stockName}</td>
      <td>
        <button type="button" className="admin-action-button" onClick={() => onEdit(stock)}>
          編集
        </button>
      </td>
      <td>
        <button type="button" className="admin-danger-button" onClick={() => onDelete(stock.id)}>
          削除
        </button>
      </td>
    </tr>
  );
}

function AdminPage() {
  const [message, setMessage] = useState("");

  const [stockPage, setStockPage] = useState(0);
  const [stockData, setStockData] = useState({
    totalCount: 0,
    page: 0,
    size: 20,
    totalPages: 0,
    items: [],
  });

  const [userPage, setUserPage] = useState(0);
  const [userData, setUserData] = useState({
    totalCount: 0,
    maxDisplayCount: 100,
    items: [],
  });

  const [modalOpen, setModalOpen] = useState(false);
  const [editingStockId, setEditingStockId] = useState(null);
  const [stockForm, setStockForm] = useState({
    tickerCode: "",
    stockName: "",
    market: 1,
    currentPrice: "",
    priceChange: "",
    changeRate: "",
    marketCap: "",
    displayOrder: 1,
  });
  const [stockFormError, setStockFormError] = useState("");

  const sensors = useSensors(useSensor(PointerSensor));

  useEffect(() => {
    loadStocks(stockPage);
  }, [stockPage]);

  useEffect(() => {
    loadUsers(userPage);
  }, [userPage]);

  async function loadStocks(page) {
    try {
      const res = await fetchAdminStocks(page, PAGE_SIZE);
      setStockData(res.data);
    } catch (error) {
      setMessage(error.message);
    }
  }

  async function loadUsers(page) {
    try {
      const res = await fetchAdminUsers(page, PAGE_SIZE);
      setUserData(res.data);
    } catch (error) {
      setMessage(error.message);
    }
  }

  function openCreateModal() {
    setEditingStockId(null);
    setStockForm({
      tickerCode: "",
      stockName: "",
      market: 1,
      currentPrice: "",
      priceChange: "",
      changeRate: "",
      marketCap: "",
      displayOrder: stockData.totalCount + 1,
    });
    setStockFormError("");
    setModalOpen(true);
  }

  function openEditModal(stock) {
    setEditingStockId(stock.id);
    setStockForm({
      id: stock.id,
      tickerCode: stock.tickerCode || "",
      stockName: stock.stockName || "",
      market: stock.market ?? 1,
      currentPrice: stock.currentPrice ?? "",
      priceChange: stock.priceChange ?? "",
      changeRate: stock.changeRate ?? "",
      marketCap: stock.marketCap ?? "",
      displayOrder: stock.displayOrder ?? 1,
    });
    setStockFormError("");
    setModalOpen(true);
  }

  function closeModal() {
    setModalOpen(false);
    setEditingStockId(null);
    setStockFormError("");
  }

  async function handleSaveStock() {
    try {
      const payload = {
        ...stockForm,
        market: Number(stockForm.market),
        currentPrice: stockForm.currentPrice === "" ? null : Number(stockForm.currentPrice),
        priceChange: stockForm.priceChange === "" ? null : Number(stockForm.priceChange),
        changeRate: stockForm.changeRate === "" ? null : Number(stockForm.changeRate),
        marketCap: stockForm.marketCap === "" ? null : Number(stockForm.marketCap),
        displayOrder: Number(stockForm.displayOrder),
      };

      if (editingStockId) {
        const res = await updateAdminStock(editingStockId, payload);
        setMessage(res.message || "更新しました。");
      } else {
        const res = await createAdminStock(payload);
        setMessage(res.message || "登録しました。");
      }

      closeModal();
      await loadStocks(stockPage);
    } catch (error) {
      setStockFormError(error.message);
    }
  }

  async function handleDeleteStock(id) {
    try {
      const res = await deleteAdminStock(id);
      setMessage(res.message || "削除しました。");
      await loadStocks(stockPage);
    } catch (error) {
      setMessage(error.message);
    }
  }

  async function handleDeleteUser(id) {
    try {
      const res = await deleteAdminUser(id);
      setMessage(res.message || "削除しました。");
      await loadUsers(userPage);
    } catch (error) {
      setMessage(error.message);
    }
  }

  async function handleDragEnd(event) {
    const { active, over } = event;
    if (!over || active.id === over.id) return;

    const oldIndex = stockData.items.findIndex((item) => item.id === active.id);
    const newIndex = stockData.items.findIndex((item) => item.id === over.id);
    if (oldIndex < 0 || newIndex < 0) return;

    const reorderedItems = arrayMove(stockData.items, oldIndex, newIndex);
    setStockData((prev) => ({ ...prev, items: reorderedItems }));

    try {
      const stockIds = reorderedItems.map((item) => item.id);
      const res = await reorderAdminStocks(stockIds);
      setMessage(res.message || "並び順を更新しました。");
      await loadStocks(stockPage);
    } catch (error) {
      setMessage(error.message);
      await loadStocks(stockPage);
    }
  }

  return (
    <div className="admin-screen">
      <Header title="管理者画面" userName="User Name" />

      <div className="admin-page-body">
        {message && <div className="admin-message">{message}</div>}

        <section className="admin-section">
          <div className="admin-section-header">
            <div className="admin-section-title">銘柄管理</div>
            <div className="admin-section-right">
              <div className="admin-count-text">現在の登録数：{stockData.totalCount}/100社</div>
              <button type="button" className="admin-primary-button" onClick={openCreateModal}>
                新規登録
              </button>
            </div>
          </div>

          <DndContext sensors={sensors} collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
            <table className="admin-table">
              <thead>
                <tr>
                  <th className="admin-col-drag">移動</th>
                  <th className="admin-col-order">順番</th>
                  <th className="admin-col-code">銘柄コード</th>
                  <th className="admin-col-name">銘柄名</th>
                  <th className="admin-col-action">編集</th>
                  <th className="admin-col-action">削除</th>
                </tr>
              </thead>
              <SortableContext items={stockData.items.map((item) => item.id)} strategy={verticalListSortingStrategy}>
                <tbody>
                  {stockData.items.length === 0 ? (
                    <tr>
                      <td colSpan="6" className="admin-empty-cell">登録銘柄はありません。</td>
                    </tr>
                  ) : (
                    stockData.items.map((stock, index) => (
                      <SortableStockRow
                        key={stock.id}
                        stock={stock}
                        rowNumber={stockPage * PAGE_SIZE + index + 1}
                        onEdit={openEditModal}
                        onDelete={handleDeleteStock}
                      />
                    ))
                  )}
                </tbody>
              </SortableContext>
            </table>
          </DndContext>

          <Pagination
            currentPage={stockPage}
            totalCount={stockData.totalCount}
            pageSize={PAGE_SIZE}
            onPageChange={setStockPage}
          />
        </section>

        <section className="admin-section">
          <div className="admin-section-header">
            <div className="admin-section-title">ユーザ管理</div>
            <div className="admin-count-text">
              表示件数：{Math.min(userData.totalCount, userData.maxDisplayCount)}/{userData.maxDisplayCount}人
            </div>
          </div>

          <table className="admin-table">
            <thead>
              <tr>
                <th className="admin-col-user-id">ID</th>
                <th className="admin-col-user-name">ユーザ名</th>
                <th className="admin-col-user-email">メールアドレス</th>
                <th className="admin-col-user-role">権限</th>
                <th className="admin-col-action">削除</th>
              </tr>
            </thead>
            <tbody>
              {userData.items.length === 0 ? (
                <tr>
                  <td colSpan="5" className="admin-empty-cell">対象ユーザはありません。</td>
                </tr>
              ) : (
                userData.items.map((user) => (
                  <tr key={user.userId}>
                    <td>{user.userId}</td>
                    <td>{user.userName}</td>
                    <td>{user.email}</td>
                    <td>{user.role}</td>
                    <td>
                      <button
                        type="button"
                        className="admin-danger-button"
                        onClick={() => handleDeleteUser(user.userId)}
                      >
                        削除
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>

          <Pagination
            currentPage={userPage}
            totalCount={Math.min(userData.totalCount, userData.maxDisplayCount)}
            pageSize={PAGE_SIZE}
            onPageChange={setUserPage}
          />
        </section>
      </div>

      {modalOpen && (
        <div className="admin-modal-overlay">
          <div className="admin-modal">
            <div className="admin-modal-header">
              <div className="admin-modal-title">{editingStockId ? "編集" : "新規登録"}</div>
              <button type="button" className="admin-close-button" onClick={closeModal}>×</button>
            </div>

            <div className="admin-modal-body">
              <div className="admin-modal-row">
                <label className="admin-modal-label">銘柄コード</label>
                <input
                  type="text"
                  value={stockForm.tickerCode}
                  onChange={(e) => setStockForm((prev) => ({ ...prev, tickerCode: e.target.value }))}
                  className="admin-modal-input"
                />
              </div>

              <div className="admin-modal-row">
                <label className="admin-modal-label">銘柄名</label>
                <input
                  type="text"
                  value={stockForm.stockName}
                  onChange={(e) => setStockForm((prev) => ({ ...prev, stockName: e.target.value }))}
                  className="admin-modal-input"
                />
              </div>

              <div className="admin-modal-row">
                <label className="admin-modal-label">市場</label>
                <input
                  type="number"
                  value={stockForm.market}
                  onChange={(e) => setStockForm((prev) => ({ ...prev, market: e.target.value }))}
                  className="admin-modal-input"
                />
              </div>

              <div className="admin-modal-row">
                <label className="admin-modal-label">現在値</label>
                <input
                  type="number"
                  value={stockForm.currentPrice}
                  onChange={(e) => setStockForm((prev) => ({ ...prev, currentPrice: e.target.value }))}
                  className="admin-modal-input"
                />
              </div>

              <div className="admin-modal-row">
                <label className="admin-modal-label">表示順</label>
                <input
                  type="number"
                  value={stockForm.displayOrder}
                  onChange={(e) => setStockForm((prev) => ({ ...prev, displayOrder: e.target.value }))}
                  className="admin-modal-input"
                />
              </div>

              <div className="admin-modal-error">{stockFormError}</div>

              <div className="admin-modal-footer">
                <button type="button" className="admin-primary-button" onClick={handleSaveStock}>
                  更新
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminPage;