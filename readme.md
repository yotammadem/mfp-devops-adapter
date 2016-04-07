#MFP 8.0 Adapter challenge

###Background

Acme systems, Foundation customer had a great mobile app project. As part of the project, they needed to add
contact list capabilities to their app. James, their mobile architect decided to implement a simple, mobile specific REST API
for the contact list app, He defined the API in his notes and sent it to the dev team for development, The team got the API specifications,
they came back to James and asked him which technology to use for implementing this API. James knew that most of his team familiar with
Java and JAX-RS, he heard about the flexibility and ease of development of MFP adapters therefore he decided that the team should use
an adapter to develop this API.

Time passed by and the team was progressing fine. When they finished the development they came back to James to demo the work.
James was pretty satisfied but during the demo, he discovered one or two defects. He told the team to fix this and make sure
that the automated tests of the API won't let this happen anymore.

Then there was a silence and Tim (from the team) said: "But James, we don't have automatic tests..."
James was really surprised to hear this but he decided not to get too angry because he didn't mention this requirement in the
first place (he thought it is obvious). Then he realised that maybe the team don't really know how to use the existing open
source testing and build tools for completing this kind of not so trivial task.

James decided to help, he knew that MFP adapters are maven project and therefore he can use all the standard tools and open source
libraries which are used by Java developers in order to implement those automatic tests. James worked about 1/2 day and came out with
thin and quite simple test infrastructure which can be used by the team to develop the API tests. He also implemented the first test out
of list of 4 scenarios he had in mind just to show them how to use the simple API.

### Your mission #1: Write automated tests
You are a developer from the contact list API dev team, your mission is to complete the task by implementing the remaining automated
tests.

The example test is written in file: [src/test/java/com/acme/apis/ContactListApiIT.java](src/test/java/com/acme/apis/ContactListApiIT.java)

In order to start, clone this repository:
```
git clone https://github.com/yotammadem/mfp-adapter-challenge.git
```

Then open the project using your favourite IDE

Make sure you start the MFP server, from the MobileFirst-8.0.0.0 folder run
```
./run.sh
```

Then go to the mfp-adapter-challenge folder and type
```
mvn install
```

This will compile the adapter, build it, deploy it and run the automated tests.


### Last but not least
The dev team completed to implement the automatic tests and all the tests are passing now :)
The came back to James, and it was decided to publish the app to the customers. The app was working fine and customers were happy
until one day...

One day they discovered a lot of bad comments and low ratings in the app store. Customers were complaining about an empty contact list,
Their information was lost!

The issue was escalated and brought to the dev team. They noticed that since the first server restart the contact list became empty.
Then, they went to the code and seen that there is a problem: The contact list is saved in memory instead of in Cloudant as was designed...
Immediately the dev team started to change their impl to use Cloudant. Luckily, they already had the automated tests so they could change the
implementation and then run the tests again to make sure the API functionality is not broken.


### Your mission #2: Change the implementation of contact list API to use Cloudant
Your mission is to change the contact list REST API implementation such that the contacts will be persisted on Cloudant instead of saved in
a static HashMap.

The getting started sample: [Cloudant Java Adapter](https://github.com/MobileFirst-Platform-Developer-Center/CloudantAdapter/tree/release80/Adapters/CloudantJava) might help.
