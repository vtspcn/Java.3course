import java.util.ArrayList;
import java.util.List;

public class Generator {

    public Generator() {
    }

    public List<Project> execute(long count){
        List<Project> projects = new ArrayList<>();


        for (long i = 0; i < count; i++) {
            long projectId = (i+1)*100;

            long teamId = projectId*10;
            long userId = projectId*10+10;
            Team team = new Team(
                    teamId,
                    "Team_"+teamId,
                    List.of(
                            new User(
                                    userId,
                                    "User_"+userId,
                                    "Password_"+userId,
                                    teamId
                            )
                    )
            );

            Project project = new Project(
                    projectId,
                    "Project_"+projectId,
                    team,
                    List.of(
                            new Task(
                                    projectId*10+20,
                                    "Project_"+projectId*10+20,
                                    "Description",
                                    new Comment(
                                            projectId*10+50,
                                            "Messaje_"+projectId*10+50,
                                            projectId*10+20
                                    ),
                                    projectId*10+20
                            )
                    )
            );

            projects.add(project);
        }
        return projects;

    }

}
