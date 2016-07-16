package com.mutech.synergy;

public class SynergyValues {

	public static class Commons{
		public static String ISUSER_LOGGEDIN="user_loggedin";
		public static String USER_EMAILID="user_emailid";
		public static String USER_PASSWORD="user_password";
		public static String USER_DEFVALUE="defvalue";
		public static String USER_DEFKEY="defkey";
		public static String USER_ROLE="Role";
		public static String USER_NAME="Name";
		public static String USER_DESIGNATION="Designation";
		public static String USER_STATUS="Status";
		public static String USER_IMAGE="Image";
		public static String FROM_ACTIVITY="from_activity";
		public static String FROM_ACTIVITY1="from_activity1";
		public static String MEMBER_NO="member_no";
		public static String CHURCH="church";
		public static String ZONE="zone";
		public static String CHURCH_GROUP="church_group";
		public static String REGION="region";

		public static String USER_ZONE="userzone";
		public static String USER_ZONE1="userzone1";
		public static String USER_SENIOR_CELL="senior_cell";
		public static String USER_CHURCH_GROUP="userchurch_group";
		public static String USER_CHURCH_GROUP1="userchurch_group1";
		public static String USER_REGION="userregion";
		public static String USER_REGION1="userregion1";
		public static String USER_CHURCH="userchurch";
		public static String USER_CHURCH1="userchurch1";
		public static String USER_PCF="pcf";

		public static String USER_TBL_LABEL_CELLS="Cells";
		public static String USER_TBL_LABEL_SRCELLS="Senior Cells";
		public static String USER_TBL_LABEL_PCF="PCFs";
		public static String USER_TBL_LABEL_CHURCH="Churches";
		public static String USER_TBL_LABEL_GRPCHURCH="Group Churches";
		public static String USER_TBL_LABEL_ZONE="Zones";
		public static String USER_TBL_LABEL_REGION="Region";

	}

	public static class SpinnerDataFlag{
		public static boolean Regions_flag = true;
		public static boolean Zones_flag = true;
		public static boolean GroupChurches_flag = true;
		public static boolean Churches_flag = true;
		public static boolean PCFs_flag = true;
		public static boolean SeniorCells_flag = true;
		public static boolean Cells_flag = true;
	}
	
	public static class GoogleApiKeys{
		public static String PLACES_API_KEY = "AIzaSyBb5cP2qd0aS1004Yxa99PSUw8FR5q2nrA";
	}

	public static class ImageUrl{
		public static String imageUrl2 = "http://loveworldsynergy.org";
		public static String imageUrl = "http://verve.indictranstech.com";
		public static String imageUrl1 = "http://192.168.5.25:7001/";
	}
	
	
	public static class Web{
		
		public static String BASE_URL2="http://loveworldsynergy.org/api/";

		public static String BASE_URL="http://verve.indictranstech.com/api/";
		
		public static String BASE_URL1="http://192.168.5.25:7001/api/";
		

		public static class LoginService{
			public static String SERVICE_URL=BASE_URL+"method/login";
			public static String USERNAME="usr";
			public static String PASSWORD="pwd";
		}

		public static class LoggedInUserInfoService{
			public static String SERVICE_URL=BASE_URL+"method/frappe.auth.get_logged_user";
		}

		public static class GetAllRegionsService{
			public static String SERVICE_URL=BASE_URL+"resource/Regions/";
		}

		public static class GetRegionByFilterService{
			public static String SERVICE_URL=BASE_URL+"resource/Regions/";
			public static String FIELDS="fields";
			public static String FILTERS="filters";
		}

		public static class AllFieldsInOneRegionService{
			public static String SERVICE_URL=BASE_URL+"resource/Regions/";
		}

		public static class GetAllZonesService{
			public static String SERVICE_URL=BASE_URL+"resource/Zones/";
		}

		public static class GetAllGroupChurchesService{
			public static String SERVICE_URL=BASE_URL+"resource/Group%20Churches/";
		}

		public static class GetAllChurchesService{
			public static String SERVICE_URL=BASE_URL+"resource/Churches/";
		}

		public static class GetAllPCFService{
			public static String SERVICE_URL=BASE_URL+"resource/PCFs/";
		}

		public static class GetAllSeniorCellService{
			public static String SERVICE_URL=BASE_URL+"resource/Senior%20Cells/";
		}

		public static class GetAllCellService{
			public static String SERVICE_URL=BASE_URL+"resource/Cells/";
		}

		public static class CreatePCFService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.create_pcf";
			public static String PCF_CODE="pcf_code";
			public static String PCF_NAME="pcf_name";
			public static String CONTACT_PHONE_NO="contact_phone_no";
			public static String CONTACT_EMAIL_ID="contact_email_id";
			public static String CHURCH="church";
			public static String CHURCH_GROUP="church_group";
			public static String REGION="region";
			public static String ZONE="zone";
			public static String DATA="data";
		}

		public static class GetTopHierarchyChurchesService{
			public static String SERVICE_URL=BASE_URL+"resource/Churches/";
			public static String FIELDS="fields";
			public static String FILTERS="filters";
		}

		public static class CreateSeniorCellService{
			//public static String SERVICE_URL=BASE_URL+"resource/Senior%20Cells/";
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.create_senior_cells/";
			public static String SENIOR_CELL_CODE="senior_cell_code";
			public static String SENIOR_CELL_NAME="senior_cell_name";
			public static String CONTACT_PHONE_NO="contact_phone_no";
			public static String CONTACT_EMAIL_ID="contact_email_id";
			public static String CHURCH="church";
			public static String CHURCH_GROUP="church_group";
			public static String REGION="region";
			public static String ZONE="zone";
			public static String PCF="pcf";
			public static String DATA="data";
		}

		public static class GetTopHierarchyPCFService{
			public static String SERVICE_URL=BASE_URL+"resource/PCFs/";
			public static String FIELDS="fields";
			public static String FILTERS="filters";
		}

		public static class CreateCellService{
			//public static String SERVICE_URL=BASE_URL+"resource/Cells/";
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.create_cells/";
			public static String CELL_CODE="cell_code";
			public static String CELL_NAME="cell_name";
			public static String CONTACT_PHONE_NO="contact_phone_no";
			public static String CONTACT_EMAIL_ID="contact_email_id";
			public static String CHURCH="church";
			public static String CHURCH_GROUP="church_group";
			public static String REGION="region";
			public static String ZONE="zone";
			public static String PCF="pcf";
			public static String SENIOR_CELL="senior_cell";
			public static String DATA="data";

		}

		public static class GetTopHierarchySeniorCellService{
			public static String SERVICE_URL=BASE_URL+"resource/Senior%20Cells/";
			public static String FIELDS="fields";
			public static String FILTERS="filters";
		}

		public static class GetUserRolesService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.user_roles/";
			public static String DATA="data";
		}

		public static class CreateCellMeetingService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.create_meetings/";
			public static String DATA="data";
		}

		public static class GetAllMeetingService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.meetings_list_new/";
			public static String DATA="data";
		}

		public static class GetMyMeetingService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.meetings_list_member_new/";
			public static String DATA="data";
		}

		public static class MarkMyMeetingAttendanceService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.mark_my_attendance/";
			public static String DATA="data";
		}

		public static class GetMeetingMemberListService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.meetings_members/";
			public static String DATA="data";
		}

		public static class MarkMeetingMemberAllAttendanceService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.meetings_attendance/";
			public static String DATA="data";
		}

		public static class GetAllEventListService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.event_list_new/";
			public static String DATA="data";
		}

		public static class ViewGivingPledge{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.view_giving_pledge_member/";
			public static String DATA="data";
		}

		public static class GetEventMemberListService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.event_participents/";
			public static String DATA="data";
		}

		public static class MarkEventMemberAttendanceService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.event_attendance/";
			public static String DATA="data";
		}

		public static class GetMyEventListService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.my_event_list/";
			public static String DATA="data";
		}

		public static class MarkMyEventAttendanceService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.my_event_attendance/";
			public static String DATA="data";
		}

		public static class CreateEventService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.create_event/";
			public static String DATA="data";
		}

		public static class GetMemberProfileService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_my_profile/";
			public static String DATA ="data";
		}

		public static class GetCellPcf{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_childerns/";
			public static String DATA ="data";
		}


		public static class GetMemberShortProfileService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_short_profile/";
			public static String DATA ="data";
		}

		public static class GetMessageLogs{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_message_log/";
			public static String DATA ="data";
		}

		public static class SendCellLeaderMsg{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.send_push_leader";
			public static String DATA ="data";
		}

		//create_member
		
		public static class create_memberService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.create_member/";
			public static String DATA ="data";
		}
		
		public static class create_ftv{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.create_ftv/";
			public static String DATA ="data";
		}

		public static class Getgetpartnershiparms{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_partnership_arms/";
			public static String DATA ="data";
		}

		public static class Getcurrency{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_currency";
			public static String DATA ="data";
		}


		public static class SaveMemberProfileService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.update_my_profile/";
			public static String DATA ="data";
		}

		public static class ShowAllMembersService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.list_members/";
			public static String DATA ="data";
		}

		public static class ShowMembersDetailsService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.list_members_details/";
			public static String DATA ="data";
		}

		public static class GetAllTasksService {
			public static String SERVICE_URL = BASE_URL
					+ "method/church_ministry.church_ministry.doctype.member.member.task_list_new/";
			public static String SERVICE_URL1 = BASE_URL
					+ "method/church_ministry.church_ministry.doctype.member.member.task_list_team_new/";
			public static String FETCH_DATA = BASE_URL
					+ "method/church_ministry.church_ministry.doctype.member.member.get_team_members/";
			public static String DATA = "data";
			
			public static String CREATE_TASK = BASE_URL
					+ "method/church_ministry.church_ministry.doctype.member.member.create_task/";
			public static String UPDATE_TASK = BASE_URL
					+ "method/church_ministry.church_ministry.doctype.member.member.task_update/";
		}

		
		public static class DashboardDataService {
			public static String SERVICE_URL = BASE_URL + "method/church_ministry.church_ministry.doctype.member.member.dashboard/";
			public static String DATA = "data";
		}

		public static class GetMembershipstrength{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.view_membership_strength/";
			public static String DATA ="data";
		}
		

		public static class Partnership_giving_or_pledge{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.partnership_arms/";
			public static String DATA ="data";
		}
		
		public static class CreatePartnershipRecord{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.create_partnership_reocrd/";
			public static String DATA ="data";
		}
		
		public static class Partnership_giving_or_pledge_getlist {
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.partnership_arms_list/";
			public static String DATA ="data";
		}
		
		public static class DashboardListDataService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_database_masters/";
			public static String DATA ="data";
		}
		
		public static class ChruchDataService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_masters/";
			public static String DATA ="data";
		}

		public static class SearchService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.search_group_member_church/";
			public static String DATA ="data";
		}

		public static class GetHigherHierarchyService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.get_hierarchy/";
			public static String DATA="data";
		}
		
		public static class GetCellDetailsService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.get_master_details/";
			public static String DATA="data";
		}

        public static class GetInvitesContactsDetailsService{
            public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.get_master_details/";
            public static String DATA="data";
        }
		
		public static class UpdateAllDetailsService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.update_master_details/";
			public static String DATA="data";
			
		}

		public static class UpdateInvitesContactsDetailsService{
			public static String SERVICE_URL=BASE_URL+"method/church_ministry.church_ministry.doctype.member.member.update_master_details/";
			public static String DATA="data";

		}

		public static class LowerHierarchyService{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_lists/";
			public static String DATA ="data";
		}
		
		public static class fillLoginSpinner{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_masters/";
			public static String DATA ="data";
		}
		
		public static class SearchServiceforSpinner{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_search_masters/";
			public static String DATA ="data";
		}

		public static class ProfilePicUploadService{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.file_upload/";
			public static String DATA ="data";
		}

		public static class GetAllMastersService{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_masters/";
			public static String DATA ="data";
		}
		
		public static class GetAllListMastersService{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.get_database_masters/";
			public static String DATA ="data";
		}
		
		
		public static class GetPartnerShipService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.partnership_arm/";
			public static String DATA ="data";
		}
		
		
		public static class ShowPartnerShipService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.partnership_arm_details/";
			public static String DATA ="data";
		}
		
		public static class UpdatePartnerShipService{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.update_partnership_arm/";
			public static String DATA ="data";
		}
		
		public static class PushNotification{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.create_push_notification/";
			public static String USERNAME="username";
			public static String PASSWORD="userpass";
			public static String DEVICE_ID="device_id";
		}
		
		public static class PushNotificationOption{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.meetings_attendance/";
			public static String DATA ="data";
		}

		public static class MessageBoardCast{		
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.message_braudcast_details/";
			public static String DATA ="data";
		}

		public static class MemberUpdate{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.update_member/";
			public static String DATA ="data";
		}

		public static class SendBoardCast{
			public static String SERVICE_URL = BASE_URL +"method/church_ministry.church_ministry.doctype.member.member.message_braudcast_send/";
			public static String USERNAME="username";
			public static String PASSWORD="userpass";
			public static String EMAIL="email";
			public static String SMS="sms";
			public static String PUSH="push";
			public static String MESSSAGE="messsage";
			public static String RECIPENTS="recipents";
			public static String DATA ="data";
		}
		
	}
}
