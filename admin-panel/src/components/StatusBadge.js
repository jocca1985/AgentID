const StatusBadge = ({ status }) => {
  const getStatusColor = (status) => {
    switch (status?.toLowerCase()) {
      case "completed":
      case "approved":
      case "active":
        return "bg-green-100 text-green-800 border-green-200"
      case "in_progress":
      case "pending":
        return "bg-yellow-100 text-yellow-800 border-yellow-200"
      case "failed":
      case "rejected":
      case "error":
        return "bg-red-100 text-red-800 border-red-200"
      case "inactive":
      case "disabled":
        return "bg-gray-100 text-gray-800 border-gray-200"
      default:
        return "bg-blue-100 text-blue-800 border-blue-200"
    }
  }

  const formatStatus = (status) => {
    if (!status) return "Unknown"
    return status.replace(/_/g, " ").replace(/\b\w/g, (l) => l.toUpperCase())
  }

  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border ${getStatusColor(status)}`}
    >
      {formatStatus(status)}
    </span>
  )
}

export default StatusBadge
