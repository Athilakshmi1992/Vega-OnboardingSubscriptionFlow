Fund OnBoarding Flow Documentation
The System is designed in such a way that a
-> Fund can be created, edited and all the funds can be viewed.
->A task can be created , edited and viewed. A task will contain one or multiple questions.
->Investors can be created , viewed .Investors hold investor type Id from InvestorType Table.
->Onboarding flows can be created , edited and all onboarding flows can be viewed.
The tasks can be selected , the investor type can be selected , fund Id can be selected and stored while creating the onboarding flow.
The fundID and investor type Id acts as a combined unique key while creating the Onboarding flow to avoid multiple onboarding flows for the same fund and investor type.
->The Subscription to a fund can be done. Multiple subscriptions to the same fund can be done by a Investor.
While Subscribing to a fund, validations are done to check if the amount is higher than or equal to the minimum amount for the fund and all the mandatory questions have answers.
->Two Endpoints to see total amount per fund and total amount per subscriptions are available.
NOTE:
In the dataInitializer class, the default data for the funds ,Investor types and Investors are inserted on application startup to avoiding creating the reference data on every startup(as H2 Database is used )
Swagger UI: http://localhost:8080/swagger-ui/index.html

E-R Diagram:
[Fund] <1-----N> [OnboardingFlow]
 [id]              [fund_id]
 [name]            [investor_type_id]
 [min_inv_amt]     [min_inv_amt]

[OnboardingFlow] <1-----N> [Subscription]
 [id]                     [onboarding_flow_id]
 [fund_id]
 [investor_type_id]

[Subscription] <N-----1> [Answer]
 [id]                [subscription_id]
 [investor_id]
 [onboarding_flow_id]

[Subscription] <N-----1> [Investor]
 [id]               [investor_id]
 [onboarding_flow_id]

[Investor] <1-----N> [Subscription]
 [id]                [investor_id]
 [details]

[Investor] <N-----1> [InvestorType]
 [id]                [investor_type_id]

[InvestorType] <1-----N> [Investor]
 [id]                [investor_type_id]

[OnboardingFlow] <N-----M> [Task]
 [id]                  [task_id]
 [fund_id]
 [investor_type_id]

[Task] <1-----N> [Question]
 [id]                [task_id]
 [text]
 [mandatory]
Sample Requests and Responses:
Get All Funds:
GET http://localhost:8080/funds
Sample Response:
[
    {
        "id": "e873583d-3bd1-45c0-b6dd-e62104e230d2",
        "name": "Sequoia Private Equity Fund",
        "minimumInvestmentAmount": 1000000.00
    },
    {
        "id": "9e5b80f4-2248-45a3-be8e-c5f5f938dd7c",
        "name": "Coinbase Venture Capital Fund",
        "minimumInvestmentAmount": 500000.00
    }
]
Get All Investors:
GET http://localhost:8080/api/investors
Sample Response:
[
    {
        "id": "3712e387-621c-4328-92c8-397ddfcda332",
        "investorType": {
            "id": "b9bc7c1c-4e3c-420e-ab85-230833d97d02",
            "type": "Individual"
        },
        "details": "{\"firstName\":\"James\",\"lastName\":\"Smith\",\"countryOfResidence\":\"UK\"}"
    },
    {
        "id": "ce4ab159-2620-476e-ab72-686b3a947586",
        "investorType": {
            "id": "7d008cda-004e-49e1-a653-22d30a2bca4c",
            "type": "Institutional"
        },
        "details": "{\"companyName\":\"Oak Capital LLC\",\"countryOfIncorporation\":\"USA\",\"directors\":[{\"firstName\":\"James\",\"lastName\":\"Smith\",\"countryOfResidence\":\"UK\"},{\"firstName\":\"Richard\",\"lastName\":\"david\",\"countryOfResidence\":\"Canada\"}]}"
    }
]

Create Tasks:
POST http://localhost:8080/api/tasks
Sample Request:
{
   
    "questions": [
        {
            
            "text": "Provide country of",
            "mandatory": true
        },
        {
           
            "text": " investor, regular investor",
            "mandatory": true
        }
    ]
}

Sample response:
{
    "id": "0a23e7ba-0338-4863-9a5a-db7f28cb5919",
    "questions": [
        {
            "id": "6c3a9afb-c40f-4ee0-83b7-4162138ddc5d",
            "text": "Provide country of",
            "mandatory": true
        },
        {
            "id": "0775adc7-291d-491f-a03a-1c99b3858b5d",
            "text": " investor, regular investor",
            "mandatory": true
        }
    ]
}

Create Onboarding Flow:
POST: http://localhost:8080/api/onboarding-flows
Sample Request:
{
    
    "fundId": "9e5b80f4-2248-45a3-be8e-c5f5f938dd7c",
    "investorTypeId": "b9bc7c1c-4e3c-420e-ab85-230833d97d02",
    "minimumInvestment": 700.00,
    "tasks": [
    {
    "id": "0a23e7ba-0338-4863-9a5a-db7f28cb5919",
    "questions": [
        {
            "id": "6c3a9afb-c40f-4ee0-83b7-4162138ddc5d",
            "text": "Provide country of",
            "mandatory": true
        },
        {
            "id": "0775adc7-291d-491f-a03a-1c99b3858b5d",
            "text": " investor, regular investor",
            "mandatory": true
        }
    ]
}
    ]
}
Sample Response:
{
    "id": "552163a2-f63f-468a-81d0-e1472dac1315",
    "fundId": "9e5b80f4-2248-45a3-be8e-c5f5f938dd7c",
    "investorTypeId": "b9bc7c1c-4e3c-420e-ab85-230833d97d02",
    "minimumInvestment": 700.00,
    "tasks": [
    {
    "id": "0a23e7ba-0338-4863-9a5a-db7f28cb5919",
    "questions": [
        {
            "id": "6c3a9afb-c40f-4ee0-83b7-4162138ddc5d",
            "text": "Provide country of",
            "mandatory": true
        },
        {
            "id": "0775adc7-291d-491f-a03a-1c99b3858b5d",
            "text": " investor, regular investor",
            "mandatory": true
        }
    ]
}
    ]
}
Subscribe To A Fund:
POST: /api/subscriptions/subscribe
{
  "investorId": "b6603ccc-a9eb-49be-8fd6-c93199b38878",
  "onBoardingFlowId": "f9de947f-b731-4ead-a313-6e6aa5ab1ec0",
  "answers": [
    {
      "questionId": "b191596a-388e-4215-b52d-2243235f9c65",
      "answerText": "answer1"
    }
  ],
  "subscribedAmount":1000010.00,
  "active":true
}
Response :Successfully  Subscribed to a fund .
Get Total Amount of all active Subscriptions Per Fund:
GET http://localhost:8080/api/subscriptions/funds
Sample Response: [
  {
    "fundId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "fundName": "string",
    "totalAmount": 0,
    "activeSubscriptions": [
      {
        "subscriptionId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "intendedAmount": 0,
        "status": true
      }
    ]
  }
]
Get Total Amount of all active Subscriptions Per Subscription:
GET: http://localhost:8080/api/subscriptions/active
Sample Response:
[
  {
    "subscriptionId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "intendedAmount": 0,
    "status": true
  }
]







