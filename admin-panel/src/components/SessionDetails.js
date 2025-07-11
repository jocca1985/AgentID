import Card from "./Card"
import StatusBadge from "./StatusBadge"
import TimeInfo from "./TimeInfo"
import ModernTable from "./ModernTable"
import { formatDateTime } from "../lib/utils"

const SessionDetails = ({ session, actionPlans, onActionPlanClick }) => {
  if (!session) return null

  const sessionActionPlans = actionPlans.filter((plan) => plan.session_id === session.session_id)

  const columns = [
    {
      key: "action_id",
      label: "Action ID",
      render: (value) => <span className="font-mono text-sm font-medium text-blue-600">{value}</span>,
    },
    {
      key: "plan",
      label: "Plan",
      render: (value) => <span className="text-sm text-gray-900">{value}</span>,
    },
    {
      key: "plan_status",
      label: "Plan Status",
      render: (value) => <StatusBadge status={value} />,
    },
    {
      key: "loop_count",
      label: "Loop Count",
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
  ];

  const renderExpandedRow = (actionPlan) => (
    <div className="space-y-3">
      <div>
        <h4 className="text-sm font-medium text-gray-700 mb-2">Full Plan Description</h4>
        <p className="text-sm text-gray-600">{actionPlan.plan}</p>
      </div>
      <div>
        <h4 className="text-sm font-medium text-gray-700 mb-2">Execution Timeline</h4>
        <TimeInfo startTime={actionPlan.start_time} endTime={actionPlan.end_time} />
      </div>
    </div>
  )

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Session Details</h1>
        <p className="mt-2 text-gray-600">Detailed view of session {session.session_id} and its action plans.</p>
      </div>

      <Card title="Session Overview">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div>
            <h3 className="text-sm font-medium text-gray-500">Session ID</h3>
            <p className="mt-1 text-sm font-mono text-gray-900">{session.session_id}</p>
          </div>
          <div>
            <h3 className="text-sm font-medium text-gray-500">Prompt</h3>
            <p className="mt-1 text-sm text-gray-900">{session.prompt}</p>
          </div>
          <div>
            <h3 className="text-sm font-medium text-gray-500">Policy Decision</h3>
            <p className="mt-1 text-sm text-gray-900">{session.policy_decision}</p>
          </div>
          <div>
            <h3 className="text-sm font-medium text-gray-500">Policy Triggered</h3>
            <p className="mt-1 text-sm text-gray-900">{session.policy_triggered}</p>
          </div>
          <div>
            <h3 className="text-sm font-medium text-gray-500">Start Time</h3>
            <p className="mt-1 text-sm text-gray-900">{formatDateTime(session.start_time)}</p>
          </div>
          <div>
            <h3 className="text-sm font-medium text-gray-500">End Time</h3>
            <p className="mt-1 text-sm text-gray-900">{formatDateTime(session.end_time)}</p>
          </div>
        </div>
      </Card>

      <div>
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Action Plans</h2>
        <ModernTable
          data={sessionActionPlans}
          columns={columns}
          searchable={true}
          sortable={true}
          expandable={true}
          onRowClick={onActionPlanClick}
          renderExpandedRow={renderExpandedRow}
        />
      </div>
    </div>
  )
}

export default SessionDetails
