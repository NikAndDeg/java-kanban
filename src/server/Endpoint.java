package server;

public enum Endpoint {
	GET_TASKS, //GET /tasks/task/
	DELETE_TASKS, //DELETE /tasks/task/
	GET_TASK_BY_ID, //GET /tasks/task/?id=
	POST_TASK, //POST /tasks/task/ Body{JSON task}
	PATCH_TASK, //PATCH /tasks/task/ Body{JSON updated task}
	DELETE_TASK_BY_ID, //DELETE /tasks/task/?id=

	GET_EPICS, //GET /tasks/epic/
	DELETE_EPICS, //DELETE /tasks/epic/
	GET_EPIC_BY_ID, //GET /tasks/epic/?id=
	POST_EPIC, //POST /tasks/epic/ Body{JSON epic}
	PATCH_EPIC, //PATCH /tasks/epic/ Body{JSON updated epic}
	DELETE_EPIC_BY_ID, //DELETE /tasks/epic/?id=

	GET_SUBTASKS_BY_EPIC_ID, //GET /tasks/subtask/?epicId=
	GET_SUBTASKS, //GET /tasks/subtask/
	GET_SUBTASK_BY_ID, //GET /tasks/subtask/?id=
	POST_SUBTASK, //POST /tasks/subtask/ Body{JSON subtask}
	PATCH_SUBTASK, //PATCH /tasks/subtask/ Body{JSON updated subtask]
	DELETE_SUBTASK_BY_ID, //DELETE /tasks/subtask/?id=

	GET_HISTORY, //GET /tasks/history/
	GET_PRIORITIZED_TASKS, //GET /tasks/

	UNSUPPORTED
}
