import UIKit
import shared

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {



    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
         let driver = DatabaseDriverFactory()
          let db = PlatformKt.createDb(dbDriverFactory: driver)
        db.mydbQueries.insertSession(trackedAt:433, Id:2828)
        db.mydbQueries.insertEvent(eventName: "login", eventType: "login", attributes: "id:7879", trackedAt: 89876576,sessionId: 2828)
        let events = db.mydbQueries.selectAll().executeAsList()
            print("all events..\(events)")

            let sessions = db.mydbQueries.selectSession().executeAsList()
                        print("all session..\(sessions)")
        sessions.forEach {
            let filteredEvents = db.mydbQueries.filterEventOnSessionId(sessionId: $0.Id).executeAsList()
            filteredEvents.forEach {
                print("event is \($0)")
            }
        }

      
            let paths = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true)
            print(paths[0])
           
        
        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

