import ModernTable from "./ModernTable"
import StatusBadge from "./StatusBadge"
import TimeInfo from "./TimeInfo"
import { formatDateTime } from "../lib/utils"

const SessionsList = ({ sessions, onSessionClick }) => {
  const columns = [
    {
      key: "session_id",
      label: "Session ID",
      render: (value) => <span className="font-mono text-sm font-medium text-blue-600">{value}</span>,
    },
    {
      key: "prompt",
      label: "Prompt",
      render: (value) => <span className="text-sm text-gray-900">{value}</span>,
    },
    {
      key: "policy_decision",
      label: "Policy Decision",
      render: (value) => <span className="text-sm text-gray-900">{value}</span>,
    },
    {
      key: "policy_triggered",
      label: "Policy Triggered",
      render: (value) => <span className="text-sm text-gray-900">{value}</span>,
    },
    {
      key: "start_time",
      label: "Start Time",
      render: (value) => <span className="text-sm text-gray-900">{formatDateTime(value)}</span>,
    },
    {
      key: "end_time",
      label: "End Time",
      render: (value) => <span className="text-sm text-gray-900">{formatDateTime(value)}</span>,
    },
  ]

  const renderExpandedRow = (session) => (
    <div className="space-y-3">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <h4 className="text-sm font-medium text-gray-700 mb-2">Session Timeline</h4>
          <TimeInfo startTime={session.start_time} endTime={session.end_time} />
        </div>
      </div>
    </div>
  )

  return (
    <div className="w-full">
      <ModernTable
        data={sessions}
        columns={columns}
        searchable={true}
        sortable={true}
        expandable={true}
        onRowClick={onSessionClick}
        renderExpandedRow={renderExpandedRow}
      />
    </div>
  )
}

export default SessionsList
