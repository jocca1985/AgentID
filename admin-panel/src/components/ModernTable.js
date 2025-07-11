import { useState } from "react"
import { Search, ChevronDown, ChevronUp, ChevronRight } from "lucide-react"

const ModernTable = ({
  data = [],
  columns,
  searchable = true,
  sortable = true,
  expandable = false,
  onRowClick,
  renderExpandedRow,
}) => {
  const [searchTerm, setSearchTerm] = useState("")
  const [sortConfig, setSortConfig] = useState({ key: null, direction: "asc" })
  const [expandedRows, setExpandedRows] = useState(new Set())

  const handleSort = (key) => {
    if (!sortable) return

    let direction = "asc"
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc"
    }
    setSortConfig({ key, direction })
  }

  const toggleRowExpansion = (rowId) => {
    const newExpandedRows = new Set(expandedRows)
    if (newExpandedRows.has(rowId)) {
      newExpandedRows.delete(rowId)
    } else {
      newExpandedRows.add(rowId)
    }
    setExpandedRows(newExpandedRows)
  }

  const filteredData = data.filter((item) =>
    searchable && searchTerm
      ? Object.values(item).some((value) => value?.toString().toLowerCase().includes(searchTerm.toLowerCase()))
      : true,
  )

  const sortedData = [...filteredData].sort((a, b) => {
    if (!sortConfig.key) return 0

    const aValue = a[sortConfig.key]
    const bValue = b[sortConfig.key]

    if (aValue < bValue) return sortConfig.direction === "asc" ? -1 : 1
    if (aValue > bValue) return sortConfig.direction === "asc" ? 1 : -1
    return 0
  })

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden w-full">
      <div className="overflow-x-auto w-full">
        <table className="w-full min-w-full">
          <thead className="bg-gray-50">
            {searchable && (
              <tr>
                <th colSpan={columns.length + (expandable ? 1 : 0)} className="p-0 bg-gray-50 w-full min-w-0 border-0" style={{width:'100%', border:0}}>
                  <div className="relative w-full min-w-0 p-4" style={{width:'100%'}}>
                    <input
                      type="text"
                      placeholder="🔍 Search..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      className="block w-full pl-6 pr-4 py-4 h-14 border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none min-w-0 bg-white text-lg shadow-none"
                      style={{ borderRadius: 8, width: '100%' }}
                    />
                  </div>
                </th>
              </tr>
            )}
            <tr>
              {expandable && <th className="w-8 px-4 py-3"></th>}
              {columns.map((column) => (
                <th
                  key={column.key}
                  className={`px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider ${
                    sortable && column.sortable !== false ? "cursor-pointer hover:bg-gray-100" : ""
                  }`}
                  onClick={() => column.sortable !== false && handleSort(column.key)}
                >
                  <div className="flex items-center space-x-1">
                    <span>{column.label}</span>
                    {sortable &&
                      column.sortable !== false &&
                      sortConfig.key === column.key &&
                      (sortConfig.direction === "asc" ? (
                        <ChevronUp className="h-3 w-3" />
                      ) : (
                        <ChevronDown className="h-3 w-3" />
                      ))}
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {sortedData.map((row, index) => {
              const rowId = row.id || index
              const isExpanded = expandedRows.has(rowId)

              return (
                <>
                  <tr
                    key={rowId}
                    className={`hover:bg-gray-50 transition-colors ${onRowClick ? "cursor-pointer" : ""}`}
                    onClick={() => onRowClick && onRowClick(row)}
                  >
                    {expandable && (
                      <td className="px-4 py-3">
                        <button
                          onClick={(e) => {
                            e.stopPropagation()
                            toggleRowExpansion(rowId)
                          }}
                          className="p-1 hover:bg-gray-200 rounded transition-colors"
                        >
                          <ChevronRight className={`h-3 w-3 transition-transform ${isExpanded ? "rotate-90" : ""}`} />
                        </button>
                      </td>
                    )}
                    {columns.map((column) => (
                      <td key={column.key} className="px-4 py-3 text-sm text-gray-900">
                        {column.render ? column.render(row[column.key], row) : row[column.key]}
                      </td>
                    ))}
                  </tr>
                  {expandable && isExpanded && renderExpandedRow && (
                    <tr>
                      <td colSpan={columns.length + 1} className="px-4 py-3 bg-gray-50">
                        {renderExpandedRow(row)}
                      </td>
                    </tr>
                  )}
                </>
              )
            })}
          </tbody>
        </table>
      </div>

      {sortedData.length === 0 && <div className="text-center py-8 text-gray-500">No data found</div>}
    </div>
  )
}

export default ModernTable
