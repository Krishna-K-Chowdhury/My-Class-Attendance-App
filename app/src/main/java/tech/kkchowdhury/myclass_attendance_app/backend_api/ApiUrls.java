package tech.kkchowdhury.myclass_attendance_app.backend_api;

public class ApiUrls {
    public static final String BASE_URL = "your_domain";
    public static final String SHOW_GRAPH = BASE_URL + "graph_retrieve.php?roll=";
    public static final String SHOW_PERSONAL_DETAILS_API1 = BASE_URL + "load_details.php?t1=";
    public static final String SHOW_PERSONAL_DETAILS_API2 = BASE_URL + "images/";
    public static final String COURSE_REG_API = BASE_URL + "course_registration.php";
    public static final String DEL_CONSENT_API = BASE_URL + "delete_attendance_data.php";
    public static final String DOWNLOAD_REPORT_API = BASE_URL + "loadpdf.php";
    public static final String FACULTY_PROFILE_API1 = BASE_URL + "faculty_details.php";
    public static final String FACULTY_PROFILE_API2 = BASE_URL + "faculty_images/";
    public static final String NEW_STUDENT_REGISTER_API = BASE_URL + "demoregister.php";
    public static final String FETCH_STUDENTS_API1 = BASE_URL + "demo.php";
    public static final String FETCH_STUDENTS_API2 = BASE_URL + "images/";
    public static final String STUDENTS_PROFILE_SEARCH_API1 = BASE_URL + "filter_students.php";
    public static final String STUDENTS_PROFILE_SEARCH_API2 = BASE_URL + "images/";
    public static final String UPLOAD_PROFILE_PIC_API = BASE_URL + "upload_image.php";
    public static final String MARK_PRESENT_API = BASE_URL + "demopresent.php";
}