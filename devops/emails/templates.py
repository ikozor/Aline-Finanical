

def pipeline_result(
        failure_stage,
        microservice,
        git_commit,
        jenkins_build_number,
        job_url,
        sonarqube_url,
        git_url
        ):
    status_text_color = "red" if failure_stage else "green"
    resulting_html = f"""
        <html>
            <head></head>
            <body>
            <img width="128" alt="Jenkins logo" src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/e9/Jenkins_logo.svg/128px-Jenkins_logo.svg.png">
                <h1 style="color:#a601fe;"> Aline Financial</h1>
                <h2>Pipeline: ik-{microservice}-microservice</h2>
                <h2>Build Number: #{jenkins_build_number}</h2>
                <h2>Commit/Image Tag: {git_commit}</h2>
                <h2>Build Status: <span style="color:{status_text_color}">{'failed' if failure_stage else 'success'}</span></h2>
    """
    if failure_stage:
        resulting_html += f"""
                    <h3> Pipeline failed in stage <span style="color:{status_text_color}">"{failure_stage}"</span>
        """

    resulting_html += f"""
                <h2>More info:</h2>
                <a href="{sonarqube_url}/dashboard?id=com.aline%3Aaline-{microservice}-microservice"><img width="200" style="margin: 0px 30px 0px 0px;" alt="Sonarqube" src="https://upload.wikimedia.org/wikipedia/commons/e/e6/Sonarqube-48x200.png"></a>
                <a href="{job_url}"><img width="200" style="margin: 0px 30px 0px 0px;" alt="Jenkins" src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Jenkins_logo_with_title.svg/799px-Jenkins_logo_with_title.svg.png"></a>
                <a href="{git_url}"><img width="200" style="margin: 0px 30px 0px 0px;" alt="Gitlab" src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/GitLab_logo.svg/512px-GitLab_logo.svg.png"></a>
                <a href="{job_url}/{jenkins_build_number}/snykReport"><img width="175" style="margin: 0px 30px 0px 0px;" alt="Snyk" src="https://i.ibb.co/92RXJnX/snyk.png"></a>
            </body>
        </html>
    """

    return resulting_html
