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
  closestCenter,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import {
  SortableContext,
  verticalListSortingStrategy,
  useSortable,
  arrayMove,
} from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import "../styles/AdminPage.css";

const USER_PAGE_SIZE = 20;
const STOCK_MAX_SIZE = 100;

function SortableStockRow({ stock, onEdit, onDelete }) {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({
    id: String(stock.id),
  });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    zIndex: isDragging ? 10 : "auto",
    position: "relative",
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      className={`admin-stock-grid-row ${isDragging ? "dragging" : ""}`}
    >
      <div className="admin-stock-grid-cell admin-stock-grid-drag">
        <button
          type="button"
          className="admin-drag-handle"
          title="ドラッグして並び替え"
          {...attributes}
          {...listeners}
        >
          ☰
        </button>
      </div>

      <div className="admin-stock-grid-cell admin-stock-grid-code">
        {stock.tickerCode}
      </div>

      <div className="admin-stock-grid-cell admin-stock-grid-name">
        {stock.stockName}
      </div>

      <div className="admin-stock-grid-cell admin-stock-grid-edit">
        <button
          type="button"
          className="admin-table-button admin-table-button-edit"
          onClick={() => onEdit(stock)}
        >
          編集
        </button>
      </div>

      <div className="admin-stock-grid-cell admin-stock-grid-delete">
        <button
          type="button"
          className="admin-table-button admin-table-button-delete"
          onClick={() => onDelete(stock.id)}
        >
          削除
        </button>
      </div>
    </div>
  );
}

function AdminPage() {
  const [activeTab, setActiveTab] = useState("stock");
  const [message, setMessage] = useState("");

  const [stockData, setStockData] = useState({
    totalCount: 0,
    page: 0,
    size: STOCK_MAX_SIZE,
    totalPages: 1,
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
  const [stockFormError, setStockFormError] = useState("");

  const [stockForm, setStockForm] = useState({
    tickerCode: "",
  });

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 3,
      },
    })
  );

  useEffect(() => {
    loadStocks();
  }, []);

  useEffect(() => {
    loadUsers(userPage);
  }, [userPage]);

  async function loadStocks() {
    try {
      const res = await fetchAdminStocks(0, STOCK_MAX_SIZE);
      const items = (res.data?.items || []).map((item) => ({
        ...item,
        id: Number(item.id),
      }));

      setStockData({
        ...res.data,
        items,
      });
      setMessage("");
    } catch (error) {
      setMessage(error.message || "取得に失敗しました。");
    }
  }

  async function loadUsers(page) {
    try {
      const res = await fetchAdminUsers(page, USER_PAGE_SIZE);
      setUserData(res.data);
      setMessage("");
    } catch (error) {
      setMessage(error.message || "取得に失敗しました。");
    }
  }

  function openCreateModal() {
    setEditingStockId(null);
    setStockForm({
      tickerCode: "",
    });
    setStockFormError("");
    setModalOpen(true);
  }

  function openEditModal(stock) {
    setEditingStockId(stock.id);
    setStockForm({
      tickerCode: stock.tickerCode ?? "",
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
    const payload = {
      id: editingStockId,
      tickerCode: stockForm.tickerCode,
    };

    try {
      if (editingStockId) {
        const res = await updateAdminStock(editingStockId, payload);
        setMessage(res.message || "更新しました。");
      } else {
        const res = await createAdminStock(payload);
        setMessage(res.message || "登録しました。");
      }

      closeModal();
      await loadStocks();
    } catch (error) {
      setStockFormError(error.message || "保存に失敗しました。");
    }
  }

  async function handleDeleteStock(id) {
    try {
      const res = await deleteAdminStock(id);
      setMessage(res.message || "削除しました。");
      await loadStocks();
    } catch (error) {
      setMessage(error.message || "削除に失敗しました。");
    }
  }

  async function handleDeleteUser(id) {
    try {
      const res = await deleteAdminUser(id);
      setMessage(res.message || "削除しました。");
      await loadUsers(userPage);
    } catch (error) {
      setMessage(error.message || "削除に失敗しました。");
    }
  }

  async function handleDragEnd(event) {
    const { active, over } = event;

    if (!over || active.id === over.id) return;

    const oldIndex = stockData.items.findIndex(
      (item) => String(item.id) === String(active.id)
    );
    const newIndex = stockData.items.findIndex(
      (item) => String(item.id) === String(over.id)
    );

    if (oldIndex < 0 || newIndex < 0) return;

    const reorderedItems = arrayMove(stockData.items, oldIndex, newIndex);

    setStockData((prev) => ({
      ...prev,
      items: reorderedItems.map((item, index) => ({
        ...item,
        displayOrder: index + 1,
      })),
    }));

    try {
      const stockIds = reorderedItems.map((item) => item.id);
      const res = await reorderAdminStocks(stockIds);
      setMessage(res.message || "並び順を更新しました。");
      await loadStocks();
    } catch (error) {
      setMessage(error.message || "並び順更新に失敗しました。");
      await loadStocks();
    }
  }

  return (
    <div className="admin-screen">
      <Header />

      <div className="admin-page-wrap">

        <div className="admin-tab-switch">
          <button
            type="button"
            className={`admin-tab-link ${activeTab === "stock" ? "active" : ""}`}
            onClick={() => setActiveTab("stock")}
          >
            銘柄編集
          </button>

          <button
            type="button"
            className={`admin-tab-link ${activeTab === "user" ? "active" : ""}`}
            onClick={() => setActiveTab("user")}
          >
            ユーザ編集
          </button>
        </div>

        {message && <div className="admin-message">{message}</div>}

        {activeTab === "stock" && (
          <div className="admin-panel">
            <div className="admin-stock-toolbar">
              <div className="admin-stock-count">
                現在の登録数：{stockData.totalCount}/100社
              </div>
              <button
                type="button"
                className="admin-new-button"
                onClick={openCreateModal}
              >
                新規登録
              </button>
            </div>

            <div className="admin-stock-grid">
              <div className="admin-stock-grid-header">
                <div className="admin-stock-grid-head admin-stock-grid-drag">移動</div>
                <div className="admin-stock-grid-head admin-stock-grid-code">銘柄コード</div>
                <div className="admin-stock-grid-head admin-stock-grid-name">銘柄名</div>
                <div className="admin-stock-grid-head admin-stock-grid-edit">編集</div>
                <div className="admin-stock-grid-head admin-stock-grid-delete">削除</div>
              </div>

              <DndContext
                sensors={sensors}
                collisionDetection={closestCenter}
                onDragEnd={handleDragEnd}
              >
                <SortableContext
                  items={stockData.items.map((item) => String(item.id))}
                  strategy={verticalListSortingStrategy}
                >
                  <div className="admin-stock-grid-body">
                    {stockData.items.length === 0 ? (
                      <div className="admin-empty-cell">登録銘柄はありません。</div>
                    ) : (
                      stockData.items.map((stock) => (
                        <SortableStockRow
                          key={stock.id}
                          stock={stock}
                          onEdit={openEditModal}
                          onDelete={handleDeleteStock}
                        />
                      ))
                    )}
                  </div>
                </SortableContext>
              </DndContext>
            </div>
          </div>
        )}

        {activeTab === "user" && (
          <div className="admin-panel">
            <div className="admin-user-toolbar">
              <div className="admin-user-count">
                表示件数：{Math.min(userData.totalCount, userData.maxDisplayCount)}/100人
              </div>
            </div>

            <table className="admin-user-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>ユーザ名</th>
                  <th>メールアドレス</th>
                  <th>権限</th>
                  <th>削除</th>
                </tr>
              </thead>
              <tbody>
                {userData.items.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="admin-empty-cell">
                      対象ユーザはありません。
                    </td>
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
                          className="admin-table-button admin-table-button-delete"
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
              pageSize={USER_PAGE_SIZE}
              onPageChange={setUserPage}
            />
          </div>
        )}
      </div>

      {modalOpen && (
        <div className="admin-modal-overlay">
          <div className="admin-modal-card">
            <button
              type="button"
              className="admin-modal-close"
              onClick={closeModal}
            >
              ×
            </button>

            <div className="admin-modal-form">
              <div className="admin-modal-field">
                <label className="admin-modal-label">銘柄コード</label>
                <input
                  type="text"
                  className="admin-modal-input"
                  value={stockForm.tickerCode}
                  onChange={(e) =>
                    setStockForm((prev) => ({ ...prev, tickerCode: e.target.value }))
                  }
                  placeholder="例: AAPL"
                />
              </div>

              <div className="admin-modal-error">{stockFormError}</div>

              <div className="admin-modal-submit-row">
                <button
                  type="button"
                  className="admin-modal-submit"
                  onClick={handleSaveStock}
                >
                  {editingStockId ? "更新" : "登録"}
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