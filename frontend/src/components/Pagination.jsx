function Pagination({ currentPage, totalCount, pageSize, onPageChange, maxVisible = 5 }) {
  const totalPages = Math.ceil(totalCount / pageSize);

  if (totalPages <= 1) return null;

  const pageNumbers = Array.from(
    { length: Math.min(totalPages, maxVisible) },
    (_, index) => index
  );

  return (
    <div className="pagination">
      {pageNumbers.map((page) => (
        <button
          key={page}
          type="button"
          className={page === currentPage ? "active" : ""}
          disabled={page === currentPage}
          onClick={() => onPageChange(page)}
        >
          {page + 1}
        </button>
      ))}
    </div>
  );
}

export default Pagination;