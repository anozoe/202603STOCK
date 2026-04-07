function Pagination({
  currentPage,
  totalCount,
  pageSize,
  onPageChange,
  maxVisible = 5,
}) {
  const totalPages = Math.ceil((totalCount || 0) / pageSize);

  if (totalPages <= 1) {
    return null;
  }

  let startPage = Math.max(0, currentPage - Math.floor(maxVisible / 2));
  let endPage = startPage + maxVisible - 1;

  if (endPage >= totalPages) {
    endPage = totalPages - 1;
    startPage = Math.max(0, endPage - maxVisible + 1);
  }

  const pages = [];
  for (let i = startPage; i <= endPage; i += 1) {
    pages.push(i);
  }

  return (
    <div className="pagination">
      <button
        type="button"
        className="page-button"
        disabled={currentPage === 0}
        onClick={() => onPageChange(currentPage - 1)}
      >
        ＜
      </button>

      {pages.map((page) => (
        <button
          key={page}
          type="button"
          className={`page-button ${currentPage === page ? "active" : ""}`}
          disabled={currentPage === page}
          onClick={() => onPageChange(page)}
        >
          {page + 1}
        </button>
      ))}

      <button
        type="button"
        className="page-button"
        disabled={currentPage === totalPages - 1}
        onClick={() => onPageChange(currentPage + 1)}
      >
        ＞
      </button>
    </div>
  );
}

export default Pagination;