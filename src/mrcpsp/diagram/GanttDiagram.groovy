package mrcpsp.diagram

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Logger
import org.gantt.generator.mrcpsp.Gantt
import org.swiftgantt.common.Time
import org.swiftgantt.model.Task

/**
 * Created by mateus on 1/31/14.
 */
class GanttDiagram {

    static final Logger log = Logger.getLogger(GanttDiagram.class)

    /*public Gantt(Task[] tasks, String filePath) {
        this.tasks = tasks;
        this.filePath = filePath;
    }*/

    /*Task task1 = new Task("1", new Time(1000), new Time(1001));
		Task task2 = new Task("2", new Time(1013), new Time(1018));
        Task task3 = new Task("3", new Time(1005), new Time(1008));

		task2.addPredecessor(task1);

        tasks = new Task[]{task1, task2, task3};*/

    //filePath = "gantt/test.png"
    //

    def generateGanttDiagram(Project project) {
        Gantt gantt
        def diagramPath = getDiagramPath(project)
        def orderedJobs = project.staggeredJobs.sort{it.id}
        def tasks = createTasksList(orderedJobs)

        gantt = new Gantt( (Task[]) tasks.toArray(), diagramPath)
        gantt.generateDiagram()

        true
    }

    def getDiagramPath(Project project) {
        def fileNameWithoutExtension = project.fileName.substring(0, project.fileName.indexOf('.'))

        UrlUtils.instance.diagramPath + "/" + fileNameWithoutExtension + ".png"
    }

    def createTasksList(List<Job> orderedJobs) {
        Task task
        def tasks = []
        def setYear = 1000

        orderedJobs.each {
            def id = it.id.toString()
            def start = new Time(it.startTime + setYear)
            def end = new Time(it.endTime + setYear)

            log.info(it)
            task = new Task(id, start, end)

            if (UrlUtils.instance.showPredecessors == PropertyConstants.TRUE) {
                it.predecessors.each {
                    task.addPredecessor(tasks[it - 1])
                }
            }

            tasks.add(task)
        }

        tasks
    }
}
