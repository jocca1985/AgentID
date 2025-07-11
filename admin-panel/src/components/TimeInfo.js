import { Clock } from "lucide-react"

const TimeInfo = ({ startTime, endTime }) => {
  const formatDateTime = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      timeZoneName: "short",
    })
  }

  return (
    <div className="flex items-start space-x-4 text-sm text-gray-600">
      <div className="flex items-center space-x-1">
        <Clock className="h-4 w-4" />
        <span className="font-medium">Start:</span>
        <span>{formatDateTime(startTime)}</span>
      </div>
      {endTime && (
        <div className="flex items-center space-x-1">
          <span className="w-10" />
          <Clock className="h-4 w-4" />
          <span className="font-medium">End:</span>
          <span>{formatDateTime(endTime)}</span>
        </div>
      )}
    </div>
  )
}

export default TimeInfo
