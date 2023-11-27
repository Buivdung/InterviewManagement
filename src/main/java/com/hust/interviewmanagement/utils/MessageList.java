package com.hust.interviewmanagement.utils;

import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


public interface MessageList {
    interface Common {
        String LOGIN_FAIL = "Invalid username/password. Please try again";
        String LOGIN_NO_ACTIVATED = "Account not activated";
        String MESSAGE = "message";
        String USERS = "users";
        String USER = "user";
        String SEARCH_RESPONSE = "searchResponse";

        String DEPARTMENTS = "departments";
        String DEPARTMENT = "department";
        String OFFER = "offer";
        String OFFERS = "offers";

        String CANDIDATE = "candidate";
        String CANDIDATES = "candidates";
        String INTERVIEW_SCHEDULE = "interviewSchedule";
        String INTERVIEW_SCHEDULES = "interviewSchedules";
        String INTERVIEW = "interview";
        String INTERVIEWS = "interviews";
        String RECRUITERS = "recruiters";
        String RECRUITER = "recruiter";
        String SKILL = "skill";
        String SKILLS = "skills";
        String BENEFITS = "benefits";
        String JOB = "job";
        String JOBS = "jobs";
        String LEVELS = "levels";
        String LEVEL = "level";
        String ALERT = "alert";
        String SKILLIDS = "skillIds";
        String INTERVIEWERS_ID = "interviewerIds";

        String BENEFITIDS = "benefitIds";
    }


    // Candidate
    interface MessageCandidate {
        String CREATE_CANDIDATE_FAIL = "Failed to created candidate";
        String CREATE_CANDIDATE_SUCCESS = "Successfully created candidate";
        String EDIT_CANDIDATE_FAIL = "Failed to updated candidate";
        String EDIT_CANDIDATE_SUCCESS = "Successfully updated candidate";

        String DELETE_CANDIDATE_SUCCESS = "Successfully deleted candidate";
        String DELETE_CANDIDATE_FAIL = "Failed to delete this candidate";
        String DUPLICATE_CANDIDATE = "Job already existed!";
        String DONT_CANDIDATE = "Don't find candidate";


    }
    //Job
    interface MessageJob {
        String CREATE_JOB_FAIL = "Failed to created job";
        String CREATE_JOB_SUCCESS = "Successfully created job";
        String EDIT_JOB_FAIL = "Failed to updated job";
        String EDIT_JOB_SUCCESS = "Successfully updated job";

        String DELETE_JOB_SUCCESS = "Successfully deleted job";
        String DELETE_JOB_FAIL = "Failed to delete this job";
        String DONT_JOB = "Don't find job";
        String VALIDATE_DATE = "eDate";
        String VALIDATE_SALARY = "eSalary";

        String M_SALARY = "Salary To must be greater than Salary From";
        String M_DATE = "End date must be after or equal to start date";
        String DUPLICATE_JOB = "Job already existed!";

    }

    // User
    interface MessageUser {
        String CREATE_USER_FAIL = "Failed to created user";
        String CREATE_USER_SUCCESS = "Successfully created user";
        String EDIT_USER_FAIL = "Failed to updated user";
        String EDIT_USER_SUCCESS = "Successfully updated user";

        String DELETE_USER_SUCCESS = "Successfully deleted user";
        String DELETE_USER_FAIL = "Failed to delete this user";
        String DONT_USER = "Don't find user";
        String EMAIL = "Email already existed!";
    }

    interface MessageDepartment {
        String DONT_DEPARTMENT = "Don't find department";
    }

    interface MessageInterview {
        //interview
        String CREATE_INTERVIEW_FAIL = "Failed to created interview schedule";
        String CREATE_INTERVIEW_SUCCESS = "Successfully created interview schedule";
        String EDIT_INTERVIEW_FAil = "Failed to updated interview schedule";
        String EDIT_INTERVIEW_SUCCESS = "Successfully updated interview schedule";

        String DELETE_INTERVIEW_SUCCESS = "Successfully deleted interview schedule";
        String DELETE_INTERVIEW_FAIL = "Failed to delete this interview schedule";

        String DONT_FOUND_INTERVIEW = "No interview schedule found";
    }

    interface MessageOffer {

        String DONT_OFFER = "No offer found";

        // offer
        String CREATE_OFFER_FAIL = "Failed to created offer";
        String CREATE_OFFER_SUCCESS = "Successfully created offer";
        String EDIT_OFFER_FAIL = "Failed to updated offer";
        String EDIT_OFFER_SUCCESS = "Successfully updated offer";

        String DELETE_OFFER_SUCCESS = "Successfully deleted offer";
        String DELETE_OFFER_FAIL = "Failed to delete this offer";
    }
}
